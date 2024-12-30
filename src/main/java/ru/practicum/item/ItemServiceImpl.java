package ru.practicum.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.practicum.booking.BookingRepository;
import ru.practicum.booking.model.Booking;
import ru.practicum.booking.model.Status;
import ru.practicum.exception.ItemNotFoundException;
import ru.practicum.exception.UnauthorizedAccessException;
import ru.practicum.item.mapper.CommentMapper;
import ru.practicum.item.mapper.ItemMapper;
import ru.practicum.item.model.Comment;
import ru.practicum.item.model.CommentDto;
import ru.practicum.item.model.Item;
import ru.practicum.item.model.ItemDto;
import ru.practicum.user.UserService;
import ru.practicum.user.model.User;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final CommentRepository commentRepository;
    private final UserService userService;
    private final BookingRepository bookingRepository;

    @Override
    public List<ItemDto> getItemsForUser(long userId) {
        log.info("Получение списка вещей для пользователя с userId: {}", userId);
        List<Item> items = itemRepository.findByUserId(userId);

        List<ItemDto> itemDto = items.stream()
                .map(item -> {
                    List<CommentDto> comments = item.getComments().stream()
                            .map(CommentMapper::toCommentDto)
                            .toList();
                    return ItemMapper.toItemDto(item, comments);
                })
                .toList();

        log.info("Найдено {} вещей для пользователя с userId: {}", itemDto.size(), userId);
        return itemDto;
    }

    @Override
    public ItemDto getItem(long itemId, long userId) {
        log.info("Получение вещи с itemId: {} для пользователя с userId: {}", itemId, userId);
        Item item = findItemById(itemId);
        List<CommentDto> comments = item.getComments().stream()
                .map(CommentMapper::toCommentDto)
                .toList();

        if (item.getUser().getId() == userId) {
            Booking lastBooking = bookingRepository.findLastBooking(itemId, LocalDateTime.now());
            Booking nextBooking = bookingRepository.findNextBooking(itemId, LocalDateTime.now());
            ItemDto itemDto = ItemMapper.toItemDto(item, comments, lastBooking, nextBooking);
            log.info("Найденый ItemDto: {} Последнее бронирование: {}, следующее бронирование: {}.",
                    itemDto, lastBooking, nextBooking);
            return itemDto;
        } else {
            ItemDto itemDto = ItemMapper.toItemDto(item, comments);
            log.info("Найденый ItemDto: {}", itemDto);
            return itemDto;
        }
    }

    @Override
    public ItemDto addItem(ItemDto itemDto, long userId) {
        log.info("Добавление itemDto: {} для userId {}", itemDto, itemDto);
        Item item = ItemMapper.toItem(itemDto);
        User user = userService.findUserById(userId);
        item.setUser(user);
        Item savedItem = itemRepository.save(item);
        log.info("Добавлен item {}", savedItem);
        return ItemMapper.toItemDto(savedItem, List.of());
    }

    @Override
    public ItemDto updateItem(long itemId, ItemDto itemDto, long userId) {
        log.info("Обновление вещи с itemId: {} пользователем с userId: {}", itemId, userId);
        Item existingItem = findItemById(itemId);
        if (existingItem.getUser().getId() != userId) {
            throw new UnauthorizedAccessException(userId);
        }
        if (itemDto.getName() != null && !itemDto.getName().trim().isEmpty()) {
            existingItem.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null && !itemDto.getDescription().trim().isEmpty()) {
            existingItem.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            existingItem.setAvailable(itemDto.getAvailable());
        }
        Item updatedItem = itemRepository.save(existingItem);
        List<CommentDto> comments = existingItem.getComments().stream()
                .map(CommentMapper::toCommentDto)
                .toList();
        log.info("Вещь с itemId: {} успешно обновлена", itemId);
        return ItemMapper.toItemDto(updatedItem, comments);
    }

    @Override
    public void deleteItemByUser(long userId, long itemId) {
        log.info("Удаление вещи с itemId: {} для пользователя с userId: {}", itemId, userId);
        itemRepository.deleteItemByUser(userId, itemId);
        log.info("Вещь с itemId: {} успешно удалена для пользователя с userId: {}", itemId, userId);
    }

    @Override
    public List<ItemDto> searchItems(String text) {
        log.info("Поиск вещей по тексту: '{}'", text);
        if (!StringUtils.hasText(text)) {
            log.debug("Поисковый текст пустой, возвращается пустой список");
            return Collections.emptyList();
        }
        String lowerCaseText = text.toLowerCase();
        List<Item> items = itemRepository.searchItems(lowerCaseText).stream()
                .filter(Item::getAvailable)
                .toList();
        List<ItemDto> itemDtos = items.stream()
                .map(item -> {
                    List<CommentDto> comments = item.getComments().stream()
                            .map(CommentMapper::toCommentDto)
                            .toList();
                    return ItemMapper.toItemDto(item, comments);
                })
                .toList();
        log.info("Найдено {} вещей по тексту: '{}'", itemDtos.size(), text);
        return itemDtos;
    }

    @Override
    public CommentDto addComment(long itemId, CommentDto commentDto, long userId) {
        log.info("Добавление комментария к вещи с itemId: {} от пользователя с userId: {}", itemId, userId);
        List<Booking> pastBookings = bookingRepository.findPastBookings(
                itemId, userId, Status.APPROVED, LocalDateTime.now());
        boolean hasPastApproval = !pastBookings.isEmpty();
        if (!hasPastApproval) {
            throw new IllegalArgumentException("Пользователь не может оставить комментарий к этой вещи");
        }

        Item item = findItemById(itemId);
        User author = userService.findUserById(userId);

        Comment comment = new Comment();
        comment.setText(commentDto.getText());
        comment.setItem(item);
        comment.setAuthor(author);
        comment.setCreated(LocalDateTime.now());

        Comment savedComment = commentRepository.save(comment);
        log.info("Комментарий успешно добавлен к вещи с itemId: {}", itemId);
        return CommentMapper.toCommentDto(savedComment);
    }

    @Override
    public Item findItemById(long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new ItemNotFoundException(itemId));
    }
}

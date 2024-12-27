package ru.practicum.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.booking.BookingRepository;
import ru.practicum.booking.model.Booking;
import ru.practicum.booking.model.Status;
import ru.practicum.exception.ItemNotFoundException;
import ru.practicum.exception.UnauthorizedAccessException;
import ru.practicum.exception.UserNotFoundException;
import ru.practicum.item.mapper.CommentMapper;
import ru.practicum.item.mapper.ItemMapper;
import ru.practicum.item.model.Comment;
import ru.practicum.item.model.CommentDto;
import ru.practicum.item.model.Item;
import ru.practicum.item.model.ItemDto;
import ru.practicum.user.UserRepository;
import ru.practicum.user.model.User;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;

    @Override
    public List<ItemDto> getItemsForUser(long userId) {
        List<Item> items = itemRepository.findByUserId(userId);
        return items.stream()
                .map(item -> {
                    List<CommentDto> comments = commentRepository.findByItemId(item.getId())
                            .stream()
                            .map(CommentMapper::toCommentDto)
                            .collect(Collectors.toList());
                    return ItemMapper.toItemDto(item, comments);
                })
                .collect(Collectors.toList());
    }

    @Override
    public ItemDto getItem(long itemId, long userId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ItemNotFoundException(itemId));
        List<CommentDto> comments = commentRepository.findByItemId(itemId)
                .stream()
                .map(CommentMapper::toCommentDto)
                .collect(Collectors.toList());

        if (item.getUser().getId() == userId) {
            Booking lastBooking = bookingRepository.findFirstByItemIdAndStartBeforeOrderByStartDesc(itemId, LocalDateTime.now());
            Booking nextBooking = bookingRepository.findFirstByItemIdAndStartAfterOrderByStartAsc(itemId, LocalDateTime.now());
            return ItemMapper.toItemDto(item, comments, lastBooking, nextBooking);
        } else {
            return ItemMapper.toItemDto(item, comments);
        }
    }

    @Override
    public ItemDto addItem(ItemDto itemDto, long userId) {
        Item item = ItemMapper.toItem(itemDto);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        item.setUser(user);
        Item savedItem = itemRepository.save(item);
        return ItemMapper.toItemDto(savedItem, List.of());
    }

    @Override
    public ItemDto updateItem(long itemId, ItemDto itemDto, long userId) {
        Item existingItem = itemRepository.findById(itemId)
                .orElseThrow(() -> new ItemNotFoundException(itemId));
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
        List<CommentDto> comments = commentRepository.findByItemId(itemId)
                .stream()
                .map(CommentMapper::toCommentDto)
                .collect(Collectors.toList());
        return ItemMapper.toItemDto(updatedItem, comments);
    }

    @Override
    public void deleteItemByUser(long userId, long itemId) {
        itemRepository.deleteItemByUser(userId, itemId);
    }

    @Override
    public List<ItemDto> searchItems(String text) {
        if (text == null || text.trim().isEmpty()) {
            return Collections.emptyList();
        }
        String lowerCaseText = text.toLowerCase();
        List<Item> items = itemRepository.searchItems(lowerCaseText).stream()
                .filter(Item::getAvailable)
                .collect(Collectors.toList());
        return items.stream()
                .map(item -> {
                    List<CommentDto> comments = commentRepository.findByItemId(item.getId())
                            .stream()
                            .map(CommentMapper::toCommentDto)
                            .collect(Collectors.toList());
                    return ItemMapper.toItemDto(item, comments);
                })
                .collect(Collectors.toList());
    }

    @Override
    public CommentDto addComment(long itemId, CommentDto commentDto, long userId) {
        boolean hasApprovedAndFinishedBooking = bookingRepository.findByItemIdAndBookerIdAndStatusAndEndBefore(
                itemId,
                userId,
                Status.APPROVED,
                LocalDateTime.now()
        ).size() > 0;

        if (!hasApprovedAndFinishedBooking) {
            throw new IllegalArgumentException("Пользователь не может оставить комментарий к этой вещи");
        }

        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ItemNotFoundException(itemId));
        User author = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        Comment comment = new Comment();
        comment.setText(commentDto.getText());
        comment.setItem(item);
        comment.setAuthor(author);
        comment.setCreated(LocalDateTime.now());

        Comment savedComment = commentRepository.save(comment);
        return CommentMapper.toCommentDto(savedComment);
    }
}

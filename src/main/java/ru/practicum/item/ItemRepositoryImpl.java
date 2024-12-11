package ru.practicum.item;

import org.springframework.stereotype.Repository;
import ru.practicum.exception.*;
import ru.practicum.item.mapper.ItemMapper;
import ru.practicum.item.model.Item;
import ru.practicum.item.model.ItemDto;
import ru.practicum.user.UserServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class ItemRepositoryImpl implements ItemRepository {
    private final List<Item> items = new ArrayList<>();
    private final AtomicLong idGenerator = new AtomicLong();
    private final UserServiceImpl userServiceImp;

    public ItemRepositoryImpl(UserServiceImpl userServiceImp) {
        this.userServiceImp = userServiceImp;
    }

    @Override
    public List<ItemDto> findByUserId(long userId) {
        List<ItemDto> userItems = new ArrayList<>();
        for (Item item : items) {
            if (item.getUserId() == userId) {
                userItems.add(ItemMapper.toItemDto(item));
            }
        }
        if (userItems.isEmpty()) {
            throw new NotFoundException("У пользователя с id " + userId + " нет элементов.");
        }
        return userItems;
    }

    @Override
    public ItemDto getItem(long itemId) {
        Item item = items.stream()
                .filter(i -> i.getId() == itemId)
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Элемент с id " + itemId + " не найден."));
        return ItemMapper.toItemDto(item);
    }

    @Override
    public ItemDto save(ItemDto itemDto, long userId) {
        if (userServiceImp.getUser(userId) == null) {
            throw new UserNotFoundException(userId);
        }
        Item item = ItemMapper.toItem(itemDto);
        item.setId(idGenerator.incrementAndGet());
        item.setUserId(userId);
        item.setAvailable(itemDto.getAvailable());
        items.add(item);
        return ItemMapper.toItemDto(item);
    }

    @Override
    public ItemDto updateItem(long itemId, ItemDto itemDto, long userId) {
        Item item = items.stream()
                .filter(i -> i.getId() == itemId && i.getUserId() == userId)
                .findFirst()
                .orElseThrow(() -> new UnauthorizedAccessException(userId));

        if (itemDto.getName() != null && !itemDto.getName().isEmpty()) {
            item.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null && !itemDto.getDescription().isEmpty()) {
            item.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            item.setAvailable(itemDto.getAvailable());
        }
        return ItemMapper.toItemDto(item);
    }

    @Override
    public void deleteItemByUser(long userId, long itemId) {
        boolean removed = items.removeIf(item -> item.getId() == itemId && item.getUserId() == userId);
        if (!removed) {
            throw new ItemNotFoundException(itemId);
        }
    }

    @Override
    public List<ItemDto> searchItems(String text) {
        if (text == null || text.isEmpty()) {
            return new ArrayList<>();
        }
        List<ItemDto> searchResults = new ArrayList<>();
        for (Item item : items) {
            if ((item.getName().toUpperCase().contains(text.toUpperCase()) ||
                    item.getDescription().toUpperCase().contains(text.toUpperCase())) &&
                    Boolean.TRUE.equals(item.getAvailable())) {
                searchResults.add(ItemMapper.toItemDto(item));
            }
        }
        return searchResults;
    }
}


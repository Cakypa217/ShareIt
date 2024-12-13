package ru.practicum.item;

import org.springframework.stereotype.Repository;
import ru.practicum.exception.*;
import ru.practicum.item.model.Item;
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
    public List<Item> findByUserId(long userId) {
        List<Item> userItems = new ArrayList<>();
        for (Item item : items) {
            if (item.getUserId() == userId) {
                userItems.add(item);
            }
        }
        if (userItems.isEmpty()) {
            throw new NotFoundException("У пользователя с id " + userId + " нет элементов.");
        }
        return userItems;
    }

    @Override
    public Item getItem(long itemId) {
        Item item = items.stream()
                .filter(i -> i.getId() == itemId)
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Элемент с id " + itemId + " не найден."));
        return item;
    }

    @Override
    public Item save(Item item, long userId) {
        if (userServiceImp.getUser(userId) == null) {
            throw new UserNotFoundException(userId);
        }
        Item itemm = item;
        item.setId(idGenerator.incrementAndGet());
        item.setUserId(userId);
        item.setAvailable(item.getAvailable());
        items.add(item);
        return itemm;
    }

    @Override
    public Item updateItem(long itemId, Item item, long userId) {
        Item item1 = items.stream()
                .filter(i -> i.getId() == itemId && i.getUserId() == userId)
                .findFirst()
                .orElseThrow(() -> new UnauthorizedAccessException(userId));

        if (item.getName() != null && !item.getName().isEmpty()) {
            item1.setName(item.getName());
        }
        if (item.getDescription() != null && !item.getDescription().isEmpty()) {
            item1.setDescription(item.getDescription());
        }
        if (item.getAvailable() != null) {
            item1.setAvailable(item.getAvailable());
        }
        return item1;
    }

    @Override
    public void deleteItemByUser(long userId, long itemId) {
        boolean removed = items.removeIf(item -> item.getId() == itemId && item.getUserId() == userId);
        if (!removed) {
            throw new ItemNotFoundException(itemId);
        }
    }

    @Override
    public List<Item> searchItems(String text) {
        if (text == null || text.isEmpty()) {
            return new ArrayList<>();
        }
        List<Item> searchResults = new ArrayList<>();
        for (Item item : items) {
            if ((item.getName().toUpperCase().contains(text.toUpperCase()) ||
                    item.getDescription().toUpperCase().contains(text.toUpperCase())) &&
                    Boolean.TRUE.equals(item.getAvailable())) {
                searchResults.add(item);
            }
        }
        return searchResults;
    }
}


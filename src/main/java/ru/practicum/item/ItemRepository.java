package ru.practicum.item;

import ru.practicum.item.model.Item;

import java.util.List;

public interface ItemRepository {
    List<Item> findByUserId(long userId);

    Item getItem(long itemId);

    Item save(Item item, long userId);

    void deleteItemByUser(long userId, long itemId);

    Item updateItem(long itemId, Item item, long userId);

    List<Item> searchItems(String text);
}

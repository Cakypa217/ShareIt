package ru.practicum.item;

import ru.practicum.item.model.Item;

import java.util.List;

public interface ItemRepository {
    List<Item> findByUserId(long userId);

    Item getItem(long itemId);

    Item save(Item item);

    void deleteItemByUser(long userId, long itemId);

    Item updateItem(Item item);

    List<Item> searchItems(String text);
}

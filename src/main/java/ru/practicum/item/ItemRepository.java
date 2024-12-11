package ru.practicum.item;

import ru.practicum.item.model.ItemDto;

import java.util.List;

public interface ItemRepository {
    List<ItemDto> findByUserId(long userId);

    ItemDto getItem(long itemId);

    ItemDto save(ItemDto itemDto, long userId);

    void deleteItemByUser(long userId, long itemId);

    ItemDto updateItem(long itemId, ItemDto itemDto, long userId);

    List<ItemDto> searchItems(String text);
}

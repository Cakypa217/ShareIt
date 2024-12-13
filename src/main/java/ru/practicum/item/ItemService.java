package ru.practicum.item;

import ru.practicum.item.model.ItemDto;

import java.util.List;

public interface ItemService {
    List<ItemDto> getItemsForUser(long userId);

    ItemDto getItem(long itemId);

    ItemDto addItem(ItemDto itemDto, long userId);

    ItemDto updateItem(long itemId, ItemDto itemDto, long userId);

    void deleteItemByUser(long userId, long itemId);

    List<ItemDto> searchItems(String text);
}

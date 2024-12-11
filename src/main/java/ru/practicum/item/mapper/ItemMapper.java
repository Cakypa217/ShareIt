package ru.practicum.item.mapper;

import lombok.NoArgsConstructor;
import ru.practicum.item.model.Item;
import ru.practicum.item.model.ItemDto;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class ItemMapper {
    public static ItemDto toItemDto(Item item) {
        ItemDto itemDto = new ItemDto();
        itemDto.setId(item.getId());
        itemDto.setName(item.getName());
        itemDto.setDescription(item.getDescription());
        itemDto.setAvailable(item.getAvailable() != null ? item.getAvailable() : true);
        return itemDto;
    }

    public static Item toItem(ItemDto itemDto) {
        Item item = new Item();
        item.setId(itemDto.getId());
        item.setName(itemDto.getName());
        item.setDescription(itemDto.getDescription());
        item.setAvailable(itemDto.getAvailable() != null ? itemDto.getAvailable() : true);
        return item;
    }
}

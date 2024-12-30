package ru.practicum.item.mapper;

import lombok.NoArgsConstructor;
import ru.practicum.booking.mapper.BookingMapper;
import ru.practicum.booking.model.Booking;
import ru.practicum.item.model.CommentDto;
import ru.practicum.item.model.Item;
import ru.practicum.item.model.ItemDto;

import java.util.List;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class ItemMapper {
    public static ItemDto toItemDto(Item item, List<CommentDto> comments) {
        ItemDto itemDto = new ItemDto();
        itemDto.setId(item.getId());
        itemDto.setName(item.getName());
        itemDto.setDescription(item.getDescription());
        itemDto.setAvailable(item.getAvailable() != null ? item.getAvailable() : true);
        itemDto.setComments(comments);
        return itemDto;
    }

    public static ItemDto toItemDto(Item item, List<CommentDto> comments, Booking lastBooking, Booking nextBooking) {
        ItemDto itemDto = toItemDto(item, comments);
        if (lastBooking != null) {
            itemDto.setLastBooking(BookingMapper.toBookingDto(lastBooking));
        }
        if (nextBooking != null) {
            itemDto.setNextBooking(BookingMapper.toBookingDto(nextBooking));
        }
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

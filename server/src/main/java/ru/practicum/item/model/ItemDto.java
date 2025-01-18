package ru.practicum.item.model;

import lombok.Data;
import ru.practicum.booking.model.BookingDto;

import java.util.List;

@Data
public class ItemDto {
    private long id;
    private String name;
    private String description;
    private Boolean available;
    private List<CommentDto> comments;
    private BookingDto lastBooking;
    private BookingDto nextBooking;
    private Long requestId;
}

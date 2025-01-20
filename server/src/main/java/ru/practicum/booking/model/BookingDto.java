package ru.practicum.booking.model;

import lombok.Data;
import ru.practicum.item.model.ItemDto;
import ru.practicum.user.model.UserDto;

import java.time.LocalDateTime;

@Data
public class BookingDto {
    private long id;
    private UserDto booker;
    private ItemDto item;
    private long itemId;
    private LocalDateTime start;
    private LocalDateTime end;
    private Status status;
}

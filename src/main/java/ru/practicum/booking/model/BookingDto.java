package ru.practicum.booking.model;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import ru.practicum.item.model.ItemDto;
import ru.practicum.user.model.UserDto;

import java.time.LocalDateTime;

@Data
public class BookingDto {
    private long id;
    @NotNull
    private UserDto booker;
    @NotNull
    private ItemDto item;
    private long itemId;
    @NotNull
    private LocalDateTime start;
    @NotNull
    private LocalDateTime end;
    @NotNull
    private Status status;
}

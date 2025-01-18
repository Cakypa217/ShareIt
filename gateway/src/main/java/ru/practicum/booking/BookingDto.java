package ru.practicum.booking;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.item.ItemDto;
import ru.practicum.user.UserDto;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingDto {
    private Long id;

    @NotNull
    private Long itemId;

    @NotNull
    @FutureOrPresent
    private LocalDateTime start;

    @NotNull
    @Future
    private LocalDateTime end;

    private UserDto booker;
    private ItemDto item;
    private Status status;

    @AssertTrue(message = "Некорректные даты бронирования")
    public boolean isValidBookingDates() {
        if (start == null || end == null) return false;
        if (end.isBefore(start)) return false;
        return !start.equals(end);
    }
}

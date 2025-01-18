package ru.practicum.booking;

import ru.practicum.booking.model.BookingDto;
import ru.practicum.booking.model.State;
import ru.practicum.booking.model.Status;

import java.util.List;

public interface BookingService {
    BookingDto addBooking(long userId, BookingDto bookingDto);

    BookingDto updateBookingStatus(long bookingId, boolean approved, long userId);

    BookingDto getBooking(long bookingId, long userId);

    List<BookingDto> getBookingsForUser(long userId, State state);

    List<BookingDto> getBookingsForOwner(long userId, Status status);

}

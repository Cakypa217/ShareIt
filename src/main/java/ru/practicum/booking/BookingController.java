package ru.practicum.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.booking.model.BookingDto;
import ru.practicum.booking.model.State;
import ru.practicum.booking.model.Status;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bookings")
public class BookingController {
    private static final String USER_ID_HEADER = "X-Sharer-User-Id";
    private final BookingService bookingService;

    @PostMapping
    public BookingDto addBooking(@RequestHeader(USER_ID_HEADER) long userId, @RequestBody BookingDto bookingDto) {
        return bookingService.addBooking(userId, bookingDto);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto updateBookingStatus(@PathVariable long bookingId, @RequestParam boolean approved,
                                          @RequestHeader(USER_ID_HEADER) long userId) {
        return bookingService.updateBookingStatus(bookingId, approved, userId);
    }

    @GetMapping("/{bookingId}")
    public BookingDto getBooking(@PathVariable long bookingId, @RequestHeader(USER_ID_HEADER) long userId) {
        return bookingService.getBooking(bookingId, userId);
    }

    @GetMapping
    public List<BookingDto> getBookingsForUser(@RequestHeader(USER_ID_HEADER) long userId,
                                               @RequestParam(defaultValue = "ALL") State state) {
        return bookingService.getBookingsForUser(userId, state);
    }

    @GetMapping("/owner")
    public List<BookingDto> getBookingsForOwner(@RequestHeader(USER_ID_HEADER) long userId,
                                                @RequestParam(defaultValue = "ALL") Status status) {
        return bookingService.getBookingsForOwner(userId, status);
    }
}

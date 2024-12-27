package ru.practicum.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.booking.model.BookingDto;
import ru.practicum.booking.model.State;
import ru.practicum.booking.model.Status;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/bookings")
public class BookingController {
    private static final String USER_ID_HEADER = "X-Sharer-User-Id";
    private final BookingService bookingService;

    @PostMapping
    public BookingDto addBooking(@RequestHeader(USER_ID_HEADER) long userId, @RequestBody BookingDto bookingDto) {
        log.info("Получен запрос POST /bookings с userId: {} и bookingDto: {}", userId, bookingDto);
        BookingDto booking = bookingService.addBooking(userId, bookingDto);
        log.info("Добавлен новый бронирование: {}", booking);
        return booking;
    }

    @PatchMapping("/{bookingId}")
    public BookingDto updateBookingStatus(@PathVariable long bookingId, @RequestParam boolean approved,
                                          @RequestHeader(USER_ID_HEADER) long userId) {
        log.info("Получен запрос PATCH /bookings/{} с approved: {} и userId: {}", bookingId, approved, userId);
        BookingDto booking = bookingService.updateBookingStatus(bookingId, approved, userId);
        log.info("Обновлено бронирование: {}", booking);
        return booking;
    }

    @GetMapping("/{bookingId}")
    public BookingDto getBooking(@PathVariable long bookingId, @RequestHeader(USER_ID_HEADER) long userId) {
        log.info("Получен запрос GET /bookings/{} с userId: {}", bookingId, userId);
        BookingDto booking = bookingService.getBooking(bookingId, userId);
        log.info("Получено бронирование: {}", booking);
        return booking;
    }

    @GetMapping
    public List<BookingDto> getBookingsForUser(@RequestHeader(USER_ID_HEADER) long userId,
                                               @RequestParam(defaultValue = "ALL") State state) {
        log.info("Получен запрос GET /bookings с userId: {} и state: {}", userId, state);
        List<BookingDto> bookings = bookingService.getBookingsForUser(userId, state);
        log.info("Получено {} бронирований для пользователя {}", bookings.size(), userId);
        return bookings;
    }

    @GetMapping("/owner")
    public List<BookingDto> getBookingsForOwner(@RequestHeader(USER_ID_HEADER) long userId,
                                                @RequestParam(defaultValue = "ALL") Status status) {
        log.info("Получен запрос GET /bookings/owner с userId: {} и state: {}", userId, status);
        List<BookingDto> bookings = bookingService.getBookingsForOwner(userId, status);
        log.info("Получено {} бронирований для владельца {}", bookings.size(), userId);
        return bookings;
    }
}

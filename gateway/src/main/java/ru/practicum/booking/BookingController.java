package ru.practicum.booking;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Validated
public class BookingController {
    private static final String USER_ID_HEADER = "X-Sharer-User-Id";
    private final BookingClient bookingClient;

    @PostMapping
    public ResponseEntity<Object> addBooking(@RequestHeader(USER_ID_HEADER) long userId,
                                             @RequestBody @Valid BookingDto bookingDto) {
        return bookingClient.createBooking(userId, bookingDto);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBooking(@RequestHeader(USER_ID_HEADER) long userId,
                                             @PathVariable Long bookingId) {
        return bookingClient.getBooking(userId, bookingId);
    }

    @GetMapping
    public ResponseEntity<Object> getBookings(@RequestHeader(USER_ID_HEADER) long userId,
                                              @RequestParam(name = "state", defaultValue = "ALL") String stateParam,
                                              @PositiveOrZero @RequestParam(
                                                      name = "from", defaultValue = "0") Integer from,
                                              @Positive @RequestParam(
                                                      name = "size", defaultValue = "10") Integer size) {
        State state = State.from(stateParam)
                .orElseThrow(() -> new IllegalArgumentException("Unknown state: " + stateParam));
        return bookingClient.getAllBookings(userId, state.name(), from, size);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> updateBooking(@RequestHeader(USER_ID_HEADER) long userId,
                                                @PathVariable Long bookingId,
                                                @RequestParam Boolean approved) {
        return bookingClient.updateBooking(userId, bookingId, approved);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getOwnerBookings(@RequestHeader(USER_ID_HEADER) long userId,
                                                   @RequestParam(
                                                           name = "state", defaultValue = "ALL") String stateParam,
                                                   @PositiveOrZero @RequestParam(
                                                           name = "from", defaultValue = "0") Integer from,
                                                   @Positive @RequestParam(
                                                           name = "size", defaultValue = "10") Integer size) {
        State state = State.from(stateParam)
                .orElseThrow(() -> new IllegalArgumentException("Unknown state: " + stateParam));
        return bookingClient.getAllBookingsForOwner(userId, state.name(), from, size);
    }

}

package ru.practicum.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.booking.mapper.BookingMapper;
import ru.practicum.booking.model.Booking;
import ru.practicum.booking.model.BookingDto;
import ru.practicum.booking.model.State;
import ru.practicum.booking.model.Status;
import ru.practicum.exception.ItemNotFoundException;
import ru.practicum.item.ItemRepository;
import ru.practicum.item.model.Item;
import ru.practicum.user.UserService;
import ru.practicum.user.model.User;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final UserService userService;
    private final ItemRepository itemRepository;

    @Override
    public BookingDto addBooking(long userId, BookingDto bookingDto) {
        log.info("Добавление нового бронирования для userId: {} с данными: {}", userId, bookingDto);
        User user = userService.findUserById(userId);
        Item item = itemRepository.findById(bookingDto.getItemId())
                .orElseThrow(() -> new ItemNotFoundException(bookingDto.getItemId()));
        if (!item.getAvailable()) {
            throw new IllegalArgumentException("Вещь недоступна для бронирования");
        }
        Booking booking = BookingMapper.toBooking(bookingDto);
        booking.setStatus(Status.WAITING);
        booking.setBooker(user);
        booking.setItem(item);
        booking = bookingRepository.save(booking);
        log.info("Бронирование добавлено: {}", booking);
        return BookingMapper.toBookingDto(booking);
    }

    @Override
    public BookingDto updateBookingStatus(long bookingId, boolean approved, long userId) {
        log.info("Обновление статуса бронирования с bookingId: {} для userId: {} на approved: {}",
                bookingId, userId, approved);
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Бронирование не найдено"));

        if (booking.getItem().getUser().getId() != userId) {
            throw new IllegalArgumentException("Пользователь не является владельцем вещи");
        }

        if (booking.getStatus() != Status.WAITING) {
            throw new IllegalArgumentException("Статус бронирования уже изменен");
        }

        booking.setStatus(approved ? Status.APPROVED : Status.REJECTED);
        booking = bookingRepository.save(booking);
        log.info("Статус бронирования обновлен: {}", booking);
        return BookingMapper.toBookingDto(booking);
    }

    @Override
    public BookingDto getBooking(long bookingId, long userId) {
        log.info("Получение бронирования с bookingId: {} для userId: {}", bookingId, userId);
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Бронирование не найдено"));
        log.info("Бронирование получено: {}", booking);
        return BookingMapper.toBookingDto(booking);
    }

    @Override
    public List<BookingDto> getBookingsForUser(long userId, State state) {
        log.info("Получение бронирований для пользователя с userId: {} и состоянием: {}", userId, state);
        userService.findUserById(userId);
        List<Booking> bookings = bookingRepository.findByBookerIdOrderByStartDesc(userId);
        log.info("Найдено {} бронирований для пользователя с userId: {}", bookings.size(), userId);
        return bookings.stream()
                .map(BookingMapper::toBookingDto)
                .toList();
    }

    @Override
    public List<BookingDto> getBookingsForOwner(long userId, Status status) {
        log.info("Получение бронирований для владельца с userId: {} и статусом: {}", userId, status);
        List<Booking> bookings = bookingRepository.findAll();
        log.info("Найдено {} бронирований для владельца с userId: {}", bookings.size(), userId);
        return bookings.stream()
                .map(BookingMapper::toBookingDto)
                .toList();
    }
}

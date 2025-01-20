package ru.practicum.booking;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.booking.model.BookingDto;
import ru.practicum.booking.model.State;
import ru.practicum.booking.model.Status;
import ru.practicum.item.ItemService;
import ru.practicum.item.model.ItemDto;
import ru.practicum.user.UserService;
import ru.practicum.user.model.UserDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class BookingServiceIntegrationTest {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private UserService userService;

    @Autowired
    private ItemService itemService;

    @Test
    public void testCreateAndGetBooking() {
        UserDto owner = new UserDto();
        owner.setName("Owner User");
        owner.setEmail("owner@example.com");
        UserDto savedOwner = userService.addUser(owner);

        UserDto booker = new UserDto();
        booker.setName("Booker User");
        booker.setEmail("booker@example.com");
        UserDto savedBooker = userService.addUser(booker);

        ItemDto itemDto = new ItemDto();
        itemDto.setName("Велосипед");
        itemDto.setDescription("Горный велосипед");
        itemDto.setAvailable(true);
        ItemDto savedItem = itemService.addItem(itemDto, savedOwner.getId(), null);

        BookingDto bookingDto = new BookingDto();
        bookingDto.setItemId(savedItem.getId());
        bookingDto.setStart(LocalDateTime.now().plusHours(1));
        bookingDto.setEnd(LocalDateTime.now().plusDays(1));

        BookingDto savedBooking = bookingService.addBooking(savedBooker.getId(), bookingDto);

        assertNotNull(savedBooking, "Сохраненное бронирование не должно быть null");
        assertNotNull(savedBooking.getId(), "ID бронирования не должен быть null");
        assertEquals(Status.WAITING, savedBooking.getStatus(), "Статус должен быть WAITING");
        assertEquals(savedItem.getId(), savedBooking.getItemId(), "ID вещи должен совпадать");

        BookingDto approvedBooking = bookingService.updateBookingStatus(savedBooking.getId(), true,
                savedOwner.getId());
        assertEquals(Status.APPROVED, approvedBooking.getStatus(), "Статус должен быть APPROVED");

        List<BookingDto> bookerBookings = bookingService.getBookingsForUser(savedBooker.getId(), State.ALL);
        assertFalse(bookerBookings.isEmpty(), "Список бронирований не должен быть пустым");
        assertEquals(1, bookerBookings.size(), "Должно быть одно бронирование");
    }
}

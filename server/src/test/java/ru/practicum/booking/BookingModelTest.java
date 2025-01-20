package ru.practicum.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.booking.model.Booking;
import ru.practicum.booking.model.Status;
import ru.practicum.item.model.Item;
import ru.practicum.user.model.User;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class BookingModelTest {
    @Autowired
    private JacksonTester<Booking> json;

    private Booking booking;
    private User booker;
    private Item item;

    @BeforeEach
    void setUp() {
        booker = new User();
        booker.setId(1L);
        booker.setName("Test User");
        booker.setEmail("test@test.com");

        item = new Item();
        item.setId(1L);
        item.setName("Test Item");
        item.setDescription("Test Description");
        item.setAvailable(true);

        booking = new Booking();
        booking.setId(1L);
        booking.setStart(LocalDateTime.of(2024, 1, 1, 12, 0));
        booking.setEnd(LocalDateTime.of(2024, 1, 2, 12, 0));
        booking.setBooker(booker);
        booking.setItem(item);
        booking.setStatus(Status.WAITING);
    }

    @Test
    void testBookingSerialization() throws Exception {
        JsonContent<Booking> result = json.write(booking);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.start").isEqualTo("2024-01-01T12:00:00");
        assertThat(result).extractingJsonPathStringValue("$.end").isEqualTo("2024-01-02T12:00:00");
        assertThat(result).extractingJsonPathNumberValue("$.booker.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.booker.name").isEqualTo("Test User");
        assertThat(result).extractingJsonPathNumberValue("$.item.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.status").isEqualTo("WAITING");
    }

    @Test
    void testBookingDeserialization() throws Exception {
        String jsonContent = """
                {
                    "id": 1,
                    "start": "2024-01-01T12:00:00",
                    "end": "2024-01-02T12:00:00",
                    "booker": {
                        "id": 1,
                        "name": "Test User",
                        "email": "test@test.com"
                    },
                    "item": {
                        "id": 1,
                        "name": "Test Item",
                        "description": "Test Description",
                        "available": true
                    },
                    "status": "WAITING"
                }
                """;

        Booking result = json.parse(jsonContent).getObject();

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getStart()).isEqualTo(LocalDateTime.of(2024, 1, 1, 12, 0));
        assertThat(result.getEnd()).isEqualTo(LocalDateTime.of(2024, 1, 2, 12, 0));
        assertThat(result.getBooker().getId()).isEqualTo(1L);
        assertThat(result.getItem().getId()).isEqualTo(1L);
        assertThat(result.getStatus()).isEqualTo(Status.WAITING);
    }

    @Test
    void testEquals() {
        Booking booking1 = new Booking();
        booking1.setId(1L);

        Booking booking2 = new Booking();
        booking2.setId(1L);

        Booking booking3 = new Booking();
        booking3.setId(2L);

        assertThat(booking1).isEqualTo(booking2);
        assertThat(booking1).isNotEqualTo(booking3);
    }

    @Test
    void testHashCode() {
        Booking booking1 = new Booking();
        booking1.setId(1L);

        Booking booking2 = new Booking();
        booking2.setId(1L);

        Booking booking3 = new Booking();
        booking3.setId(2L);

        assertThat(booking1.hashCode()).isEqualTo(booking2.hashCode());
        assertThat(booking1.hashCode()).isNotEqualTo(booking3.hashCode());
    }
}

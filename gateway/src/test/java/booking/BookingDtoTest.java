package booking;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import org.springframework.test.context.ContextConfiguration;
import ru.practicum.ShareItGateway;
import ru.practicum.booking.BookingDto;
import ru.practicum.booking.Status;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@JsonTest
@ContextConfiguration(classes = {ShareItGateway.class})
public class BookingDtoTest {
    @Autowired
    private JacksonTester<BookingDto> json;

    @Test
    void testBookingDtoSerialization() throws Exception {
        LocalDateTime start = LocalDateTime.of(2024, 1, 1, 12, 0);
        LocalDateTime end = LocalDateTime.of(2024, 1, 2, 12, 0);

        BookingDto bookingDto = new BookingDto();
        bookingDto.setId(1L);
        bookingDto.setStart(start);
        bookingDto.setEnd(end);
        bookingDto.setItemId(1L);
        bookingDto.setStatus(Status.WAITING);

        JsonContent<BookingDto> result = json.write(bookingDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.start").isEqualTo("2024-01-01T12:00:00");
        assertThat(result).extractingJsonPathStringValue("$.end").isEqualTo("2024-01-02T12:00:00");
        assertThat(result).extractingJsonPathNumberValue("$.itemId").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.status").isEqualTo("WAITING");
    }

    @Test
    void testBookingDtoDeserialization() throws Exception {
        String jsonContent = "{\"id\":1,\"itemId\":1,\"start\":\"2024-01-01T12:00:00\",\"" +
                "end\":\"2024-01-02T12:00:00\",\"status\":\"WAITING\"}";

        BookingDto result = json.parse(jsonContent).getObject();

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getItemId()).isEqualTo(1L);
        assertThat(result.getStart()).isEqualTo(LocalDateTime.of(2024, 1, 1, 12, 0));
        assertThat(result.getEnd()).isEqualTo(LocalDateTime.of(2024, 1, 2, 12, 0));
        assertThat(result.getStatus()).isEqualTo(Status.WAITING);
    }

    @Test
    void testValidBookingDates() {
        BookingDto bookingDto = new BookingDto();
        bookingDto.setStart(LocalDateTime.now().plusDays(1));
        bookingDto.setEnd(LocalDateTime.now().plusDays(2));

        assertTrue(bookingDto.isValidBookingDates());
    }

    @Test
    void testInvalidBookingDates() {
        BookingDto bookingDto = new BookingDto();
        LocalDateTime sameTime = LocalDateTime.now().plusDays(1);
        bookingDto.setStart(sameTime);
        bookingDto.setEnd(sameTime);

        assertFalse(bookingDto.isValidBookingDates());
    }
}

package booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.bind.MethodArgumentNotValidException;
import ru.practicum.ShareItGateway;
import ru.practicum.booking.BookingClient;
import ru.practicum.booking.BookingController;
import ru.practicum.booking.BookingDto;
import ru.practicum.booking.Status;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookingController.class)
@ContextConfiguration(classes = ShareItGateway.class)
class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookingClient bookingClient;

    @Autowired
    private ObjectMapper mapper;

    private BookingDto bookingDto;
    private static final String USER_ID_HEADER = "X-Sharer-User-Id";

    @BeforeEach
    void setUp() {
        bookingDto = new BookingDto();
        bookingDto.setId(1L);
        bookingDto.setStart(LocalDateTime.now().plusHours(1));
        bookingDto.setEnd(LocalDateTime.now().plusDays(1));
        bookingDto.setItemId(1L);
        bookingDto.setStatus(Status.WAITING);
        mapper.registerModule(new JavaTimeModule());
    }

    @Test
    void addBooking_WithValidData_ShouldReturnCreatedBooking() throws Exception {
        when(bookingClient.createBooking(anyLong(), any(BookingDto.class)))
                .thenReturn(ResponseEntity.ok(bookingDto));

        mockMvc.perform(MockMvcRequestBuilders.post("/bookings")
                        .header(USER_ID_HEADER, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(bookingDto)))
                .andExpect(status().isOk());

        verify(bookingClient).createBooking(eq(1L), any(BookingDto.class));
    }

    @Test
    void updateBooking_WithValidData_ShouldReturnUpdatedBooking() throws Exception {
        when(bookingClient.updateBooking(anyLong(), anyLong(), anyBoolean()))
                .thenReturn(ResponseEntity.ok(bookingDto));

        mockMvc.perform(MockMvcRequestBuilders.patch("/bookings/{bookingId}", 1L)
                        .header(USER_ID_HEADER, 1L)
                        .param("approved", "true"))
                .andExpect(status().isOk());

        verify(bookingClient).updateBooking(1L, 1L, true);
    }

    @Test
    void getBooking_WithValidId_ShouldReturnBooking() throws Exception {
        when(bookingClient.getBooking(anyLong(), anyLong()))
                .thenReturn(ResponseEntity.ok(bookingDto));

        mockMvc.perform(MockMvcRequestBuilders.get("/bookings/{bookingId}", 1L)
                        .header(USER_ID_HEADER, 1L))
                .andExpect(status().isOk());

        verify(bookingClient).getBooking(1L, 1L);
    }

    @Test
    void getBookings_WithValidUserId_ShouldReturnBookingsList() throws Exception {
        when(bookingClient.getAllBookings(anyLong(), anyString(), anyInt(), anyInt()))
                .thenReturn(ResponseEntity.ok(List.of(bookingDto)));

        mockMvc.perform(MockMvcRequestBuilders.get("/bookings")
                        .header(USER_ID_HEADER, 1L)
                        .param("state", "ALL")
                        .param("from", "0")
                        .param("size", "10"))
                .andExpect(status().isOk());

        verify(bookingClient).getAllBookings(1L, "ALL", 0, 10);
    }

    @Test
    void getOwnerBookings_WithValidUserId_ShouldReturnBookingsList() throws Exception {
        when(bookingClient.getAllBookingsForOwner(anyLong(), anyString(), anyInt(), anyInt()))
                .thenReturn(ResponseEntity.ok(List.of(bookingDto)));

        mockMvc.perform(MockMvcRequestBuilders.get("/bookings/owner")
                        .header(USER_ID_HEADER, 1L)
                        .param("state", "ALL")
                        .param("from", "0")
                        .param("size", "10"))
                .andExpect(status().isOk());

        verify(bookingClient).getAllBookingsForOwner(1L, "ALL", 0, 10);
    }

    @Test
    void addBooking_WithoutUserId_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(bookingDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void addBooking_WithInvalidDates_ShouldReturnBadRequest() throws Exception {
        bookingDto.setStart(LocalDateTime.now());
        bookingDto.setEnd(LocalDateTime.now());

        mockMvc.perform(MockMvcRequestBuilders.post("/bookings")
                        .header(USER_ID_HEADER, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(bookingDto)))
                .andExpect(status().isBadRequest());
    }


    @Test
    void getBookings_WithValidState_ShouldReturnOk() throws Exception {
        when(bookingClient.getAllBookings(anyLong(), anyString(), anyInt(), anyInt()))
                .thenReturn(ResponseEntity.ok(List.of(bookingDto)));

        mockMvc.perform(MockMvcRequestBuilders.get("/bookings")
                        .header(USER_ID_HEADER, 1L)
                        .param("state", "ALL")
                        .param("from", "0")
                        .param("size", "10"))
                .andExpect(status().isOk());

        verify(bookingClient).getAllBookings(1L, "ALL", 0, 10);
    }

    @Test
    void addBooking_WithValidDates_ShouldReturnOk() throws Exception {
        BookingDto bookingDto = new BookingDto();
        bookingDto.setItemId(1L);
        bookingDto.setStart(LocalDateTime.now().plusDays(1));
        bookingDto.setEnd(LocalDateTime.now().plusDays(2));

        when(bookingClient.createBooking(anyLong(), any(BookingDto.class)))
                .thenReturn(ResponseEntity.ok(bookingDto));

        mockMvc.perform(MockMvcRequestBuilders.post("/bookings")
                        .header(USER_ID_HEADER, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(bookingDto)))
                .andExpect(status().isOk());

        verify(bookingClient).createBooking(anyLong(), any(BookingDto.class));
    }

    @Test
    void addBooking_WithNullDates_ShouldReturnBadRequest() throws Exception {
        BookingDto bookingDto = new BookingDto();
        bookingDto.setItemId(1L);
        bookingDto.setStart(null);
        bookingDto.setEnd(null);

        mockMvc.perform(MockMvcRequestBuilders.post("/bookings")
                        .header(USER_ID_HEADER, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(bookingDto)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException()
                        instanceof MethodArgumentNotValidException))
                .andExpect(result -> {
                    MethodArgumentNotValidException exception = (MethodArgumentNotValidException)
                            result.getResolvedException();
                    assertNotNull(exception);
                    assertTrue(exception.getBindingResult().hasFieldErrors("start"));
                    assertTrue(exception.getBindingResult().hasFieldErrors("end"));
                });

        verify(bookingClient, never()).createBooking(anyLong(), any(BookingDto.class));
    }
}

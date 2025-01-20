package request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.ShareItGateway;
import ru.practicum.request.ItemRequestDto;
import ru.practicum.request.RequestClient;
import ru.practicum.request.RequestController;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RequestController.class)
@ContextConfiguration(classes = ShareItGateway.class)
class RequestControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RequestClient requestClient;

    @Autowired
    private ObjectMapper mapper;

    private ItemRequestDto itemRequestDto;
    private static final String USER_ID_HEADER = "X-Sharer-User-Id";

    @BeforeEach
    void setUp() {
        itemRequestDto = new ItemRequestDto();
        itemRequestDto.setId(1L);
        itemRequestDto.setDescription("Test request description");
        itemRequestDto.setCreated(LocalDateTime.now());
        itemRequestDto.setItems(new ArrayList<>());
    }

    @Test
    void addRequest_WithValidData_ShouldReturnCreatedRequest() throws Exception {
        when(requestClient.addRequest(anyLong(), any(ItemRequestDto.class)))
                .thenReturn(ResponseEntity.ok(itemRequestDto));

        mockMvc.perform(post("/requests")
                        .header(USER_ID_HEADER, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(itemRequestDto)))
                .andExpect(status().isOk());

        verify(requestClient).addRequest(eq(1L), any(ItemRequestDto.class));
    }

    @Test
    void addRequest_WithoutUserId_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(post("/requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(itemRequestDto)))
                .andExpect(status().isBadRequest());
    }


    @Test
    void getUserRequests_WithValidUserId_ShouldReturnRequestsList() throws Exception {
        when(requestClient.getUserRequests(anyLong()))
                .thenReturn(ResponseEntity.ok(new ArrayList<>()));

        mockMvc.perform(get("/requests")
                        .header(USER_ID_HEADER, 1L))
                .andExpect(status().isOk());

        verify(requestClient).getUserRequests(1L);
    }

    @Test
    void getUserRequests_WithoutUserId_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(get("/requests"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAllRequests_WithValidUserId_ShouldReturnRequestsList() throws Exception {
        when(requestClient.getAllRequests(anyLong()))
                .thenReturn(ResponseEntity.ok(new ArrayList<>()));

        mockMvc.perform(get("/requests/all")
                        .header(USER_ID_HEADER, 1L))
                .andExpect(status().isOk());

        verify(requestClient).getAllRequests(1L);
    }

    @Test
    void getAllRequests_WithoutUserId_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(get("/requests/all"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getRequestById_WithValidIds_ShouldReturnRequest() throws Exception {
        when(requestClient.getRequestById(anyLong(), anyLong()))
                .thenReturn(ResponseEntity.ok(itemRequestDto));

        mockMvc.perform(get("/requests/{requestId}", 1L)
                        .header(USER_ID_HEADER, 1L))
                .andExpect(status().isOk());

        verify(requestClient).getRequestById(1L, 1L);
    }

    @Test
    void getRequestById_WithoutUserId_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(get("/requests/{requestId}", 1L))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getRequestById_WithInvalidRequestId_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(get("/requests/{requestId}", "invalid")
                        .header(USER_ID_HEADER, 1L))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getRequestById_WithNegativeRequestId_ShouldReturnBadRequest() throws Exception {
        when(requestClient.getRequestById(anyLong(), anyLong()))
                .thenReturn(ResponseEntity.ok(itemRequestDto));

        mockMvc.perform(get("/requests/{requestId}", -1L)
                        .header(USER_ID_HEADER, 1L))
                .andExpect(status().isOk());
    }

    @Test
    void getRequestById_WithNegativeUserId_ShouldReturnBadRequest() throws Exception {
        when(requestClient.getRequestById(anyLong(), anyLong()))
                .thenReturn(ResponseEntity.ok(itemRequestDto));

        mockMvc.perform(get("/requests/{requestId}", 1L)
                        .header(USER_ID_HEADER, -1L))
                .andExpect(status().isOk());
    }
}

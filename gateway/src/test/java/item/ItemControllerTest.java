package item;

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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.practicum.ShareItGateway;
import ru.practicum.item.CommentDto;
import ru.practicum.item.ItemClient;
import ru.practicum.item.ItemController;
import ru.practicum.item.ItemDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemController.class)
@ContextConfiguration(classes = ShareItGateway.class)
class ItemControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ItemClient itemClient;

    @Autowired
    private ObjectMapper mapper;

    private ItemDto itemDto;
    private CommentDto commentDto;

    private static final String USER_ID_HEADER = "X-Sharer-User-Id";

    @BeforeEach
    void setUp() {
        itemDto = new ItemDto();
        itemDto.setId(1L);
        itemDto.setName("Test item");
        itemDto.setDescription("Test description");
        itemDto.setAvailable(true);

        commentDto = new CommentDto();
        commentDto.setId(1L);
        commentDto.setText("Test comment");
        commentDto.setAuthorName("Test Author");
        commentDto.setCreated(LocalDateTime.now().toString());
    }

    @Test
    void addItem_WithValidData_ShouldReturnCreatedItem() throws Exception {
        when(itemClient.createItem(anyLong(), any(ItemDto.class)))
                .thenReturn(ResponseEntity.ok(itemDto));

        mockMvc.perform(post("/items")
                        .header(USER_ID_HEADER, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(itemDto)))
                .andExpect(status().isOk());

        verify(itemClient).createItem(eq(1L), any(ItemDto.class));
    }

    @Test
    void updateItem_WithValidData_ShouldReturnUpdatedItem() throws Exception {
        when(itemClient.updateItem(anyLong(), anyLong(), any(ItemDto.class)))
                .thenReturn(ResponseEntity.ok(itemDto));

        mockMvc.perform(patch("/items/{itemId}", 1L)
                        .header(USER_ID_HEADER, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(itemDto)))
                .andExpect(status().isOk());

        verify(itemClient).updateItem(eq(1L), eq(1L), any(ItemDto.class));
    }

    @Test
    void getItem_WithValidId_ShouldReturnItem() throws Exception {
        when(itemClient.getItem(anyLong(), anyLong()))
                .thenReturn(ResponseEntity.ok(itemDto));

        mockMvc.perform(MockMvcRequestBuilders.get("/items/{itemId}", 1L)
                        .header(USER_ID_HEADER, 1L))
                .andExpect(status().isOk());

        verify(itemClient).getItem(1L, 1L);
    }

    @Test
    void getUserItems_WithValidUserId_ShouldReturnItemsList() throws Exception {
        when(itemClient.getUserItems(anyLong()))
                .thenReturn(ResponseEntity.ok(List.of(itemDto)));

        mockMvc.perform(MockMvcRequestBuilders.get("/items")
                        .header(USER_ID_HEADER, 1L))
                .andExpect(status().isOk());

        verify(itemClient).getUserItems(1L);
    }

    @Test
    void searchItems_WithValidQuery_ShouldReturnItemsList() throws Exception {
        when(itemClient.searchItems(anyString()))
                .thenReturn(ResponseEntity.ok(List.of(itemDto)));

        mockMvc.perform(MockMvcRequestBuilders.get("/items/search")
                        .param("text", "test"))
                .andExpect(status().isOk());

        verify(itemClient).searchItems("test");
    }

    @Test
    void addComment_WithValidData_ShouldReturnCreatedComment() throws Exception {
        when(itemClient.addComment(anyLong(), anyLong(), any(CommentDto.class)))
                .thenReturn(ResponseEntity.ok(commentDto));

        mockMvc.perform(post("/items/{itemId}/comment", 1L)
                        .header(USER_ID_HEADER, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(commentDto)))
                .andExpect(status().isOk());

        verify(itemClient).addComment(eq(1L), eq(1L), any(CommentDto.class));
    }

    @Test
    void deleteItem_WithValidIds_ShouldReturnOk() throws Exception {
        when(itemClient.deleteItem(anyLong(), anyLong()))
                .thenReturn(ResponseEntity.ok().build());

        mockMvc.perform(MockMvcRequestBuilders.delete("/items/{itemId}", 1L)
                        .header(USER_ID_HEADER, 1L))
                .andExpect(status().isOk());

        verify(itemClient).deleteItem(1L, 1L);
    }

    @Test
    void deleteItem_WithoutUserId_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/items/{itemId}", 1L))
                .andExpect(status().isBadRequest());
    }
}

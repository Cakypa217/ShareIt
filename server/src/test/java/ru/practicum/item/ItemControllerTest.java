package ru.practicum.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.practicum.item.model.CommentDto;
import ru.practicum.item.model.ItemDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemController.class)
class ItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ItemService itemService;

    @Autowired
    private ObjectMapper objectMapper;

    private ItemDto itemDto;
    private CommentDto commentDto;

    @BeforeEach
    void setUp() {
        itemDto = new ItemDto();
        itemDto.setId(1L);
        itemDto.setName("Test Item");
        itemDto.setDescription("Test Description");
        itemDto.setAvailable(true);

        commentDto = new CommentDto();
        commentDto.setId(1L);
        commentDto.setText("Test Comment");
        commentDto.setAuthorName("Test Author");
        commentDto.setCreated(LocalDateTime.parse(LocalDateTime.now().toString()));
    }

    @Test
    void addItem_ShouldReturnCreated() throws Exception {
        when(itemService.addItem(any(ItemDto.class), anyLong(), isNull()))
                .thenReturn(itemDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/items")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemDto)))
                .andExpect(status().isOk());

        verify(itemService).addItem(any(ItemDto.class), anyLong(), isNull());
    }

    @Test
    void updateItem_ShouldReturnOk() throws Exception {
        when(itemService.updateItem(anyLong(), any(ItemDto.class), anyLong()))
                .thenReturn(itemDto);

        mockMvc.perform(MockMvcRequestBuilders.patch("/items/{itemId}", 1L)
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemDto)))
                .andExpect(status().isOk());

        verify(itemService).updateItem(anyLong(), any(ItemDto.class), anyLong());
    }

    @Test
    void getItem_ShouldReturnOk() throws Exception {
        when(itemService.getItem(anyLong(), anyLong()))
                .thenReturn(itemDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/items/{itemId}", 1L)
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk());

        verify(itemService).getItem(anyLong(), anyLong());
    }

    @Test
    void getItemsForUser_ShouldReturnOk() throws Exception {
        when(itemService.getItemsForUser(anyLong()))
                .thenReturn(List.of(itemDto));

        mockMvc.perform(MockMvcRequestBuilders.get("/items")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk());

        verify(itemService).getItemsForUser(anyLong());
    }

    @Test
    void searchItems_ShouldReturnOk() throws Exception {
        when(itemService.searchItems(anyString()))
                .thenReturn(List.of(itemDto));

        mockMvc.perform(MockMvcRequestBuilders.get("/items/search")
                        .param("text", "test"))
                .andExpect(status().isOk());

        verify(itemService).searchItems(anyString());
    }

    @Test
    void addComment_ShouldReturnCreated() throws Exception {
        when(itemService.addComment(anyLong(), any(CommentDto.class), anyLong()))
                .thenReturn(commentDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/items/{itemId}/comment", 1L)
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentDto)))
                .andExpect(status().isOk());

        verify(itemService).addComment(anyLong(), any(CommentDto.class), anyLong());
    }

    @Test
    void deleteItemByUserId_ShouldReturnOk() throws Exception {
        long userId = 1L;
        long itemId = 1L;

        mockMvc.perform(MockMvcRequestBuilders.delete("/items/{userId}/{itemId}", userId, itemId))
                .andExpect(status().isOk());

        verify(itemService).deleteItemByUser(userId, itemId);
    }
}

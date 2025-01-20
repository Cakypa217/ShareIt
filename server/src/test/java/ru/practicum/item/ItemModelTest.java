package ru.practicum.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.item.model.Comment;
import ru.practicum.item.model.Item;
import ru.practicum.request.model.ItemRequest;
import ru.practicum.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class ItemModelTest {
    @Autowired
    private JacksonTester<Item> json;

    private Item item;
    private User owner;
    private ItemRequest request;
    private Comment comment;

    @BeforeEach
    void setUp() {
        owner = new User();
        owner.setId(1L);
        owner.setName("Test Owner");
        owner.setEmail("owner@test.com");

        request = new ItemRequest();
        request.setId(1L);
        request.setDescription("Test Request");
        request.setCreated(LocalDateTime.now());

        comment = new Comment();
        comment.setId(1L);
        comment.setText("Test Comment");
        comment.setCreated(LocalDateTime.now());
        comment.setAuthor(owner);

        item = new Item();
        item.setId(1L);
        item.setName("Test Item");
        item.setDescription("Test Description");
        item.setAvailable(true);
        item.setUser(owner);
        item.setRequest(request);
        item.setComments(List.of(comment));
    }

    @Test
    void testItemSerialization() throws Exception {
        JsonContent<Item> result = json.write(item);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("Test Item");
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo(
                "Test Description");
        assertThat(result).extractingJsonPathBooleanValue("$.available").isTrue();

        assertThat(result).extractingJsonPathNumberValue("$.user.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.user.name").isEqualTo("Test Owner");
        assertThat(result).extractingJsonPathStringValue("$.user.email").isEqualTo("owner@test.com");

        assertThat(result).extractingJsonPathNumberValue("$.request.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.request.description").isEqualTo(
                "Test Request");

        assertThat(result).extractingJsonPathNumberValue("$.comments[0].id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.comments[0].text").isEqualTo(
                "Test Comment");
    }

    @Test
    void testItemDeserialization() throws Exception {
        String jsonContent = """
                {
                    "id": 1,
                    "name": "Test Item",
                    "description": "Test Description",
                    "available": true,
                    "user": {
                        "id": 1,
                        "name": "Test Owner",
                        "email": "owner@test.com"
                    },
                    "request": {
                        "id": 1,
                        "description": "Test Request"
                    },
                    "comments": [{
                        "id": 1,
                        "text": "Test Comment",
                        "created": "2024-01-01T12:00:00"
                    }]
                }
                """;

        Item result = json.parse(jsonContent).getObject();

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("Test Item");
        assertThat(result.getDescription()).isEqualTo("Test Description");
        assertThat(result.getAvailable()).isTrue();

        assertThat(result.getUser().getId()).isEqualTo(1L);
        assertThat(result.getUser().getName()).isEqualTo("Test Owner");
        assertThat(result.getUser().getEmail()).isEqualTo("owner@test.com");

        assertThat(result.getRequest().getId()).isEqualTo(1L);
        assertThat(result.getRequest().getDescription()).isEqualTo("Test Request");

        assertThat(result.getComments()).hasSize(1);
        assertThat(result.getComments().get(0).getId()).isEqualTo(1L);
        assertThat(result.getComments().get(0).getText()).isEqualTo("Test Comment");
    }

    @Test
    void testItemEquals() {
        Item item1 = new Item();
        item1.setId(1L);

        Item item2 = new Item();
        item2.setId(1L);

        Item item3 = new Item();
        item3.setId(2L);

        assertThat(item1).isEqualTo(item2);
        assertThat(item1).isNotEqualTo(item3);
    }

    @Test
    void testItemHashCode() {
        Item item1 = new Item();
        item1.setId(1L);

        Item item2 = new Item();
        item2.setId(1L);

        Item item3 = new Item();
        item3.setId(2L);

        assertThat(item1.hashCode()).isEqualTo(item2.hashCode());
        assertThat(item1.hashCode()).isNotEqualTo(item3.hashCode());
    }

    @Test
    void testCommentEquals() {
        Comment comment1 = new Comment();
        comment1.setId(1L);

        Comment comment2 = new Comment();
        comment2.setId(1L);

        Comment comment3 = new Comment();
        comment3.setId(2L);

        assertThat(comment1).isEqualTo(comment2);
        assertThat(comment1).isNotEqualTo(comment3);
    }

    @Test
    void testCommentHashCode() {
        Comment comment1 = new Comment();
        comment1.setId(1L);

        Comment comment2 = new Comment();
        comment2.setId(1L);

        Comment comment3 = new Comment();
        comment3.setId(2L);

        assertThat(comment1.hashCode()).isEqualTo(comment2.hashCode());
        assertThat(comment1.hashCode()).isNotEqualTo(comment3.hashCode());
    }
}

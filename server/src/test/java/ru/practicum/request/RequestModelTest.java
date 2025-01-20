package ru.practicum.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.request.model.ItemRequest;
import ru.practicum.user.model.User;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class RequestModelTest {
    @Autowired
    private JacksonTester<ItemRequest> json;

    private ItemRequest itemRequest;
    private User requestor;

    @BeforeEach
    void setUp() {
        requestor = new User();
        requestor.setId(1L);
        requestor.setName("Test Requestor");
        requestor.setEmail("requestor@test.com");

        itemRequest = new ItemRequest();
        itemRequest.setId(1L);
        itemRequest.setDescription("Test Request");
        itemRequest.setRequestor(requestor);
        itemRequest.setCreated(LocalDateTime.of(2024, 1, 1, 12, 0));
    }

    @Test
    void testRequestSerialization() throws Exception {
        JsonContent<ItemRequest> result = json.write(itemRequest);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("Test Request");
        assertThat(result).extractingJsonPathNumberValue("$.requestor.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.created").isEqualTo(
                "2024-01-01T12:00:00");
    }

    @Test
    void testRequestDeserialization() throws Exception {
        String jsonContent = """
                {
                    "id": 1,
                    "description": "Test Request",
                    "requestor": {
                        "id": 1,
                        "name": "Test Requestor",
                        "email": "requestor@test.com"
                    },
                    "created": "2024-01-01T12:00:00"
                }
                """;

        ItemRequest result = json.parse(jsonContent).getObject();

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getDescription()).isEqualTo("Test Request");
        assertThat(result.getRequestor().getId()).isEqualTo(1L);
        assertThat(result.getCreated()).isEqualTo(LocalDateTime.of(
                2024, 1, 1, 12, 0));
    }

    @Test
    void testEquals() {
        ItemRequest request1 = new ItemRequest();
        request1.setId(1L);

        ItemRequest request2 = new ItemRequest();
        request2.setId(1L);

        ItemRequest request3 = new ItemRequest();
        request3.setId(2L);

        assertThat(request1).isEqualTo(request2);
        assertThat(request1).isNotEqualTo(request3);
    }

    @Test
    void testHashCode() {
        ItemRequest request1 = new ItemRequest();
        request1.setId(1L);

        ItemRequest request2 = new ItemRequest();
        request2.setId(1L);

        ItemRequest request3 = new ItemRequest();
        request3.setId(2L);

        assertThat(request1.hashCode()).isEqualTo(request2.hashCode());
        assertThat(request1.hashCode()).isNotEqualTo(request3.hashCode());
    }
}

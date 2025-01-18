package item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.test.context.ContextConfiguration;
import ru.practicum.ShareItGateway;
import ru.practicum.item.CommentDto;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@ContextConfiguration(classes = {ShareItGateway.class})
class CommentDtoTest {
    @Autowired
    private JacksonTester<CommentDto> json;

    private CommentDto commentDto;

    @BeforeEach
    void setUp() {
        commentDto = new CommentDto();
        commentDto.setId(1L);
        commentDto.setText("Test Comment");
        commentDto.setAuthorName("Test Author");
        commentDto.setCreated(LocalDateTime.now().toString());
    }

    @Test
    void testCommentDtoSerialization() throws Exception {
        JsonContent<CommentDto> result = json.write(commentDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.text").isEqualTo("Test Comment");
        assertThat(result).extractingJsonPathStringValue("$.authorName").isEqualTo("Test Author");
        assertThat(result).hasJsonPathValue("$.created");
    }
}

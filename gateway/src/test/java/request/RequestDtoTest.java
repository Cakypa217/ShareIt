package request;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.test.context.ContextConfiguration;
import ru.practicum.ShareItGateway;
import ru.practicum.request.ItemRequestDto;
import ru.practicum.user.UserDto;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@ContextConfiguration(classes = {ShareItGateway.class})
class RequestDtoTest {
    @Autowired
    private JacksonTester<ItemRequestDto> json;

    private ItemRequestDto requestDto;

    @BeforeEach
    void setUp() {
        UserDto requestor = new UserDto();
        requestor.setId(1L);
        requestor.setName("Test User");

        requestDto = new ItemRequestDto();
        requestDto.setId(1L);
        requestDto.setDescription("Test Request");
        requestDto.setRequestor(requestor);
        requestDto.setCreated(LocalDateTime.now());
        requestDto.setItems(new ArrayList<>());
    }

    @Test
    void testItemRequestDtoSerialization() throws Exception {
        JsonContent<ItemRequestDto> result = json.write(requestDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("Test Request");
        assertThat(result).hasJsonPathValue("$.created");
        assertThat(result).extractingJsonPathNumberValue("$.requestor.id").isEqualTo(1);
    }
}

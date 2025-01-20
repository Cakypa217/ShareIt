package user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.test.context.ContextConfiguration;
import ru.practicum.ShareItGateway;
import ru.practicum.user.CreateUserDto;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@ContextConfiguration(classes = {ShareItGateway.class})
class CreateUserDtoTest {
    @Autowired
    private JacksonTester<CreateUserDto> json;

    private CreateUserDto userDto;

    @BeforeEach
    void setUp() {
        userDto = CreateUserDto.builder()
                .id(1L)
                .name("Test User")
                .email("test@test.com")
                .build();
    }

    @Test
    void testCreateUserDtoSerialization() throws Exception {
        JsonContent<CreateUserDto> result = json.write(userDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("Test User");
        assertThat(result).extractingJsonPathStringValue("$.email").isEqualTo("test@test.com");
    }

    @Test
    void testCreateUserDtoDeserialization() throws Exception {
        String jsonContent = """
                {
                    "id": 1,
                    "name": "Test User",
                    "email": "test@test.com"
                }
                """;

        CreateUserDto result = json.parse(jsonContent).getObject();

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("Test User");
        assertThat(result.getEmail()).isEqualTo("test@test.com");
    }
}

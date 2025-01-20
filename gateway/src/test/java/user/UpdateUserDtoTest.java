package user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.test.context.ContextConfiguration;
import ru.practicum.ShareItGateway;
import ru.practicum.user.UpdateUserDto;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.ConstraintViolation;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@ContextConfiguration(classes = {ShareItGateway.class})
class UpdateUserDtoTest {
    @Autowired
    private JacksonTester<UpdateUserDto> json;

    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = factory.getValidator();

    @Test
    void testUpdateUserDtoSerialization() throws Exception {
        UpdateUserDto userDto = new UpdateUserDto();
        userDto.setId(1L);
        userDto.setName("Test User");
        userDto.setEmail("test@example.com");

        JsonContent<UpdateUserDto> result = json.write(userDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("Test User");
        assertThat(result).extractingJsonPathStringValue("$.email").isEqualTo("test@example.com");
    }

    @Test
    void testUpdateUserDtoDeserialization() throws Exception {
        String jsonContent = "{\"id\":1,\"name\":\"Test User\",\"email\":\"test@example.com\"}";

        UpdateUserDto result = json.parse(jsonContent).getObject();

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("Test User");
        assertThat(result.getEmail()).isEqualTo("test@example.com");
    }

    @Test
    void testValidEmail() {
        UpdateUserDto userDto = new UpdateUserDto();
        userDto.setEmail("test@example.com");

        Set<ConstraintViolation<UpdateUserDto>> violations = validator.validate(userDto);

        assertThat(violations).isEmpty();
    }

    @Test
    void testInvalidEmail() {
        UpdateUserDto userDto = new UpdateUserDto();
        userDto.setEmail("invalid-email");

        Set<ConstraintViolation<UpdateUserDto>> violations = validator.validate(userDto);

        assertThat(violations).hasSize(1);
        ConstraintViolation<UpdateUserDto> violation = violations.iterator().next();
        assertThat(violation.getMessage()).isEqualTo("Некорректный формат email");
        assertThat(violation.getPropertyPath().toString()).isEqualTo("email");
    }

    @Test
    void testNullEmail() {
        UpdateUserDto userDto = new UpdateUserDto();
        userDto.setEmail(null);

        Set<ConstraintViolation<UpdateUserDto>> violations = validator.validate(userDto);

        assertThat(violations).isEmpty();
    }
}

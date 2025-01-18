package ru.practicum.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.user.model.UserDto;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class UserServiceIntegrationTest {

    @Autowired
    private UserService userService;

    @Test
    public void testAddAndGetUser() {
        UserDto newUserDto = new UserDto();
        newUserDto.setName("Иван Петров");
        newUserDto.setEmail("ivan.petrov@example.com");

        UserDto savedUserDto = userService.addUser(newUserDto);

        assertNotNull(savedUserDto, "Сохраненный пользователь не должен быть null");
        assertNotNull(savedUserDto.getId(), "ID сохраненного пользователя не должен быть null");
        assertTrue(savedUserDto.getId() > 0, "ID должен быть положительным числом");

        UserDto retrievedUserDto = userService.getUser(savedUserDto.getId());

        assertEquals(savedUserDto.getId(), retrievedUserDto.getId(), "ID пользователя должен совпадать");
        assertEquals("Иван Петров", retrievedUserDto.getName(), "Имя пользователя должно совпадать");
        assertEquals("ivan.petrov@example.com", retrievedUserDto.getEmail(),
                "Email пользователя должен совпадать");
    }
}


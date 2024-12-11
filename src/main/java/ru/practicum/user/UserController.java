package ru.practicum.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.user.model.UserDto;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @GetMapping
    public List<UserDto> getAllUsers() {
        log.info("Получен запрос GET /users");
        List<UserDto> users = userService.getUsers();
        log.info("Найдены пользователи: {}", users);
        return users;
    }

    @GetMapping("/{userId}")
    public UserDto getUser(@PathVariable Long userId) {
        log.info("Получен запрос GET /users/{}", userId);
        UserDto user = userService.getUser(userId);
        log.info("Найден пользователь: {}", user);
        return user;
    }

    @PostMapping
    public UserDto saveNewUser(@Valid @RequestBody UserDto userDto) {
        log.info("Получен запрос POST /users с пользователем: {}", userDto);
        UserDto user = userService.addUser(userDto);
        log.info("Добавлен новый пользователь: {}", user);
        return user;
    }

    @PatchMapping("/{userId}")
    public UserDto updateUser(@Valid @PathVariable Long userId, @RequestBody UserDto userDto) {
        log.info("Получен запрос PATCH /users/{} с пользователем: {}", userId, userDto);
        UserDto user = userService.updateUser(userId, userDto);
        log.info("Обновлен пользователь: {}", userDto);
        return user;
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable Long userId) {
        log.info("Получен запрос DELETE /users/{}", userId);
        userService.deleteUser(userId);
    }
}

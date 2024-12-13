package ru.practicum.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.user.model.User;
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
        UserDto newUser = userService.addUser(userDto);
        log.info("Добавлен новый пользователь: {}", newUser);
        return newUser;
    }

    @PatchMapping("/{userId}")
    public UserDto updateUser(@Valid @PathVariable Long userId, @RequestBody UserDto userDto) {
        log.info("Получен запрос PATCH /users/{} с пользователем: {}", userId, userDto);
        UserDto newUser = userService.updateUser(userId, userDto);
        log.info("Обновлен пользователь: {}", newUser);
        return newUser;
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable Long userId) {
        log.info("Получен запрос DELETE /users/{}", userId);
        userService.deleteUser(userId);
        log.info("Пользователь с id {} удален", userId);
    }
}

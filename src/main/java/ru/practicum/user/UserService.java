package ru.practicum.user;

import ru.practicum.user.model.User;
import ru.practicum.user.model.UserDto;

import java.util.List;

public interface UserService {
    List<UserDto> getUsers();

    UserDto getUser(Long userId);

    UserDto addUser(User user);

    UserDto updateUser(Long userId, User user);

    void deleteUser(Long userId);
}

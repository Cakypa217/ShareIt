package ru.practicum.user;

import ru.practicum.user.model.User;
import ru.practicum.user.model.UserDto;

import java.util.List;

public interface UserRepository {
    List<User> findAll();

    UserDto getUser(Long userId);

    User save(User user);

    User updateUser(Long userId, User user);

    void deleteUser(Long userId);
}

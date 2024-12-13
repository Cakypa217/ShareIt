package ru.practicum.user;

import ru.practicum.user.model.User;
import ru.practicum.user.model.UserDto;

import java.util.List;

public interface UserRepository {
    List<User> findAll();

    User getUser(Long userId);

    User save(User user);

    User updateUser(User user);

    void deleteUser(Long userId);
}

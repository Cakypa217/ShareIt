package ru.practicum.user;

import ru.practicum.user.model.UserDto;

import java.util.List;

public interface UserRepository {
    List<UserDto> findAll();

    UserDto getUser(Long userId);

    UserDto save(UserDto userDto);

    UserDto updateUser(Long userId, UserDto user);

    void deleteUser(Long userId);
}

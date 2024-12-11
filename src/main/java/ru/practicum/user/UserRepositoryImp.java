package ru.practicum.user;

import org.springframework.stereotype.Repository;
import ru.practicum.exception.DuplicateException;
import ru.practicum.exception.UserNotFoundException;
import ru.practicum.user.mapper.UserMapper;
import ru.practicum.user.model.User;
import ru.practicum.user.model.UserDto;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class UserRepositoryImp implements UserRepository {
    private final List<User> users = new ArrayList<>();
    private final AtomicLong idGenerator = new AtomicLong();

    @Override
    public List<UserDto> findAll() {
        List<UserDto> allUsers = new ArrayList<>();
        for (User user1 : users) {
            UserDto userDto = UserMapper.toUserDto(user1);
            allUsers.add(userDto);
        }
        return allUsers;
    }

    @Override
    public UserDto getUser(Long userId) {
        User user = users.stream()
                .filter(user1 -> user1.getId().equals(userId))
                .findFirst()
                .orElseThrow(() -> new UserNotFoundException(userId));
        return UserMapper.toUserDto(user);
    }

    @Override
    public UserDto save(UserDto userDto) {
        if (containsEmail(userDto.getEmail())) {
            throw new DuplicateException("Пользователь с такой почтой уже зарегистрирован");
        }
        User user = UserMapper.toUser(userDto);
        user.setId(idGenerator.incrementAndGet());
        users.add(user);
        UserDto userDto1 = UserMapper.toUserDto(user);
        return userDto1;
    }

    @Override
    public UserDto updateUser(Long userId, UserDto userDto) {
        User user = users.stream()
                .filter(user1 -> user1.getId().equals(userId))
                .findFirst()
                .orElseThrow(() -> new UserNotFoundException(userId));

        if (userDto.getName() != null && !userDto.getName().isBlank()) {
            user.setName(userDto.getName());
        }

        if (userDto.getEmail() != null && !userDto.getEmail().isBlank()) {
            if (!user.getEmail().equals(userDto.getEmail()) && containsEmail(userDto.getEmail())) {
                throw new DuplicateException("Пользователь с такой почтой уже зарегистрирован");
            }
            user.setEmail(userDto.getEmail());
        }

        return UserMapper.toUserDto(user);
    }

    @Override
    public void deleteUser(Long userId) {
        if (users.stream().noneMatch(user -> user.getId().equals(userId))) {
            throw new UserNotFoundException(userId);
        }
        users.removeIf(user -> user.getId().equals(userId));
    }

    public Boolean containsEmail(String email) {
        return users.stream().anyMatch(user -> user.getEmail().equals(email));
    }
}

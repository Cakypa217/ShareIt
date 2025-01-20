package ru.practicum.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.exception.UserNotFoundException;
import ru.practicum.user.mapper.UserMapper;
import ru.practicum.user.model.User;
import ru.practicum.user.model.UserDto;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public List<UserDto> getUsers() {
        log.info("Получение всех пользователей");
        List<User> users = userRepository.findAll();
        List<UserDto> userDto = users.stream()
                .map(UserMapper::toUserDto)
                .toList();
        log.info("Найдено пользователей: {}", userDto.size());
        return userDto;
    }

    @Override
    public UserDto getUser(Long userId) {
        log.info("Получение пользователя с id: {}", userId);
        User user = findUserById(userId);
        UserDto userDto = UserMapper.toUserDto(user);
        log.info("Найден пользователь: {}", userDto);
        return userDto;
    }

    @Override
    public UserDto addUser(UserDto userDto) {
        log.info("Добавление нового пользователя: {}", userDto);
        User user = UserMapper.toUser(userDto);
        User newUser = userRepository.save(user);
        UserDto newUserDto = UserMapper.toUserDto(newUser);
        log.info("Пользователь добавлен: {}", newUserDto);
        return newUserDto;
    }

    @Override
    public UserDto updateUser(Long userId, UserDto userDto) {
        log.info("Обновление пользователя с id: {}", userId);
        User existingUser = findUserById(userId);
        if (userDto.getName() != null) {
            existingUser.setName(userDto.getName());
        }
        if (userDto.getEmail() != null) {
            existingUser.setEmail(userDto.getEmail());
        }
        User updatedUser = userRepository.save(existingUser);
        UserDto updatedUserDto = UserMapper.toUserDto(updatedUser);
        log.info("Пользователь обновлен: {}", updatedUserDto);
        return updatedUserDto;
    }

    @Override
    public void deleteUser(Long userId) {
        log.info("Удаление пользователя с id: {}", userId);
        userRepository.deleteById(userId);
        log.info("Пользователь с id {} удален", userId);
    }

    @Override
    public User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
    }
}

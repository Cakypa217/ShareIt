package ru.practicum.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.user.mapper.UserMapper;
import ru.practicum.user.model.User;
import ru.practicum.user.model.UserDto;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepositoryImp repositoryImp;

    @Override
    public List<UserDto> getUsers() {
        List<User> users = repositoryImp.findAll();
        return users.stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto getUser(Long userId) {
        User user = repositoryImp.getUser(userId);
        return UserMapper.toUserDto(user);
    }

    @Override
    public UserDto addUser(UserDto userDto) {
        User user = UserMapper.toUser(userDto);
        User newUser = repositoryImp.save(user);
        return UserMapper.toUserDto(newUser);
    }

    @Override
    public UserDto updateUser(Long userId, UserDto userDto) {
        User user = UserMapper.toUser(userDto);
        user.setId(userId);
        User newUser = repositoryImp.updateUser(user);
        return UserMapper.toUserDto(newUser);
    }

    @Override
    public void deleteUser(Long userId) {
        repositoryImp.deleteUser(userId);
    }
}

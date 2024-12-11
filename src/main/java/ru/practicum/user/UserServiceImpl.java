package ru.practicum.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.user.model.UserDto;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepositoryImp repositoryImp;

    @Override
    public List<UserDto> getUsers() {
        List<UserDto> users = repositoryImp.findAll();
        return users;
    }

    @Override
    public UserDto getUser(Long userId) {
        UserDto user = repositoryImp.getUser(userId);
        return user;
    }

    @Override
    public UserDto addUser(UserDto userDto) {
        UserDto user = repositoryImp.save(userDto);
        return user;
    }

    @Override
    public UserDto updateUser(Long userId, UserDto userDto) {
        return repositoryImp.updateUser(userId, userDto);
    }

    @Override
    public void deleteUser(Long userId) {
        repositoryImp.deleteUser(userId);
    }
}

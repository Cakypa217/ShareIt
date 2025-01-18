package ru.practicum.user.mapper;

import lombok.NoArgsConstructor;
import ru.practicum.user.model.User;
import ru.practicum.user.model.UserDto;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class UserMapper {
    public static UserDto toUserDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());
        return userDto;
    }

    public static User toUser(UserDto userDto) {
        User user = new User();
        user.setId(userDto.getId());
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        return user;
    }
}

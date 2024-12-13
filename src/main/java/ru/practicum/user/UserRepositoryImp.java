package ru.practicum.user;

import org.springframework.stereotype.Repository;
import ru.practicum.exception.DuplicateException;
import ru.practicum.exception.UserNotFoundException;
import ru.practicum.user.mapper.UserMapper;
import ru.practicum.user.model.User;
import ru.practicum.user.model.UserDto;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class UserRepositoryImp implements UserRepository {
    private final Map<Long, User> users = new HashMap<>();
    private final Set<String> emailUniqSet = new HashSet<>();
    private final AtomicLong idGenerator = new AtomicLong();

    @Override
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User getUser(Long userId) {
        User user = users.get(userId);
        if (user == null) {
            throw new UserNotFoundException(userId);
        }
        return user;
    }

    @Override
    public User save(User user) {
        final String email = user.getEmail();
        if (emailUniqSet.contains(email)) {
            throw new DuplicateException("Данный email уже используется");
        }
        user.setId(idGenerator.incrementAndGet());
        users.put(user.getId(), user);
        emailUniqSet.add(email);
        return user;
    }

    @Override
    public User updateUser(User user) {
        final Long userId = user.getId();
        User updateUser = users.get(userId);
        if (updateUser == null) {
            throw new UserNotFoundException(userId);
        }

        if (user.getName() != null && !user.getName().isBlank()) {
            updateUser.setName(user.getName());
        }

        if (user.getEmail() != null && !user.getEmail().isBlank()) {
            if (!updateUser.getEmail().equals(user.getEmail()) && emailUniqSet.contains(user.getEmail())) {
                throw new DuplicateException("Данный email уже используется");
            }
            emailUniqSet.remove(updateUser.getEmail());
            updateUser.setEmail(user.getEmail());
            emailUniqSet.add(user.getEmail());
        }

        users.put(userId, updateUser);
        return updateUser;
    }

    @Override
    public void deleteUser(Long userId) {
        User user = users.remove(userId);
        if (user == null) {
            throw new UserNotFoundException(userId);
        }
        emailUniqSet.remove(user.getEmail());
    }
}

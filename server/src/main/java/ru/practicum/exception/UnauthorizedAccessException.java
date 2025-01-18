package ru.practicum.exception;

public class UnauthorizedAccessException extends RuntimeException {
    public UnauthorizedAccessException(long userId) {
        super("Пользователь с id " + userId + " не имеет прав на выполнение этого действия");
    }
}

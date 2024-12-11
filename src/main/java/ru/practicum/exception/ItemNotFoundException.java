package ru.practicum.exception;

public class ItemNotFoundException extends RuntimeException {
    public ItemNotFoundException(long itemId) {
        super("Элемент с id " + itemId + " не найден");
    }
}

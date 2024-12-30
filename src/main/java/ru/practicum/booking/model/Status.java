package ru.practicum.booking.model;

public enum Status {
    WAITING,   // Новое бронирование, ожидает одобрения
    APPROVED,  // Бронирование подтверждено владельцем
    REJECTED,  // Бронирование отклонено владельцем
    CANCELED   // Бронирование отменено создателем
}

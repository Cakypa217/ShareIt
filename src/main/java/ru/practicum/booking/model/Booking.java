package ru.practicum.booking.model;

import lombok.Data;

@Data
public class Booking {
    private long id;
    private long booker;
    private long itemId;
    private String start;
    private String end;
    private Status status;
}

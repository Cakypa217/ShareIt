package ru.practicum.request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ItemRequest {
    private long id;
    private String description;
    private long requestor;
    private LocalDate created;

    public boolean isEmpty() {
        return id == 0 && requestor == 0 && (description == null || description.isEmpty());
    }
}

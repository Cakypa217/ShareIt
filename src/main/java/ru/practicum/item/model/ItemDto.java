package ru.practicum.item.model;

import lombok.Data;

@Data
public class ItemDto {
    private long id;
    private String name;
    private String description;
    private Boolean available;
}

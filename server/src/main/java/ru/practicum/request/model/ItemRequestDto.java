package ru.practicum.request.model;

import lombok.Data;
import ru.practicum.item.model.ItemDto;
import ru.practicum.user.model.UserDto;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ItemRequestDto {
    private long id;
    private String description;
    private UserDto requestor;
    private LocalDateTime created;
    private List<ItemDto> items;
}

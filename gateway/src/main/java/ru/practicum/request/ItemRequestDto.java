package ru.practicum.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.item.ItemDto;
import ru.practicum.user.UserDto;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemRequestDto {
    private long id;

    @NotNull(message = "Описание запроса не может быть пустым")
    private String description;

    private UserDto requestor;

    private LocalDateTime created;

    private List<ItemDto> items;
}

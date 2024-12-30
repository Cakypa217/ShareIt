package ru.practicum.request.model;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ItemRequestDto {
    private long id;
    @NotNull
    private String description;
    @NotNull
    private long requestor;
}

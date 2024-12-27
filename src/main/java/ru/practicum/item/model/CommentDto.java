package ru.practicum.item.model;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentDto {
    private long id;
    @NotNull
    private String text;
    @NotNull
    private long itemId;
    @NotNull
    private long authorId;
    private String authorName;
    private LocalDateTime created;
}

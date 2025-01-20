package ru.practicum.user;

import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class UpdateUserDto {
    private Long id;
    private String name;

    @Email(message = "Некорректный формат email")
    private String email;
}

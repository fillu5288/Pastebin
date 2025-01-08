package com.example.pastebin.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {
    @NotEmpty
    @Size(min = 3, max = 30, message = "Имя должно быть больше 3 и меньше 30")
    private String name;
}

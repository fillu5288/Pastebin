package com.example.pastebin.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
public class PostDTO {
    @NotNull
    private String message;

    @NotNull(message = "Должен быть указан user!")
    private UserDTO user;

    @UpdateTimestamp
    private LocalDateTime createdAt;
}
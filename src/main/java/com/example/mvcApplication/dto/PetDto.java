package com.example.mvcApplication.dto;

import jakarta.annotation.Nonnull;
import jakarta.validation.constraints.NotBlank;

public record PetDto(
        Long id,
        @NotBlank(message = "Name can't be empty")
        String name,
        @Nonnull
        Long userId
) {
}

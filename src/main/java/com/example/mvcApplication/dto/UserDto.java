package com.example.mvcApplication.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record UserDto(
        Long id,
        @NotBlank(message = "Name can't be empty")
        String name,
        @Email(message = "Email must be correct")
        String email,
        @Min(value = 14, message = "Age must be older than 14")
        Integer age,
        @NotNull
        List<PetDto> pets
) {
}
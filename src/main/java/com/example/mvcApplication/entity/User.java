package com.example.mvcApplication.entity;

import java.util.List;

public record User(
        Long id,
        String name,
        String email,
        Integer age,
        List<Pet> pets) {
}

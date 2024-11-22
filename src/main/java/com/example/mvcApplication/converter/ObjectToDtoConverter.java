package com.example.mvcApplication.converter;

import com.example.mvcApplication.dto.PetDto;
import com.example.mvcApplication.dto.UserDto;
import com.example.mvcApplication.entity.Pet;
import com.example.mvcApplication.entity.User;
import org.springframework.stereotype.Component;


@Component
public class ObjectToDtoConverter {

    public User toUserObject(UserDto userDto) {
        return new User(
                userDto.id(),
                userDto.name(),
                userDto.email(),
                userDto.age(),
                userDto.pets().stream().map(this::toPetObject).toList()
        );
    }

    public Pet toPetObject(PetDto petDto) {
        return new Pet(
                petDto.id(),
                petDto.name(),
                petDto.userId()
        );
    }

    public UserDto toUserDto(User user) {
        return new UserDto(
                user.id(),
                user.name(),
                user.email(),
                user.age(),
                user.pets().stream().map(this::toPetDto).toList()
        );
    }

    public PetDto toPetDto(Pet pet) {
        return new PetDto(
                pet.id(),
                pet.name(),
                pet.userId()
        );
    }
}

package com.example.mvcApplication.controller;


import com.example.mvcApplication.converter.ObjectToDtoConverter;
import com.example.mvcApplication.dto.UserDto;
import com.example.mvcApplication.entity.User;
import com.example.mvcApplication.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/users")
@RestController
public class UserController {

    private final UserService userService;
    private final ObjectToDtoConverter converter;
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    public UserController(UserService userService, ObjectToDtoConverter converter) {
        this.userService = userService;
        this.converter = converter;
    }

    @PostMapping
    public ResponseEntity<UserDto> createUser(
            @RequestBody @Validated UserDto userDto) {
        User newUser = userService.createUser(converter.toUserObject(userDto));

        log.info("Get request to create user = {}", userDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(converter.toUserDto(newUser));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> findUserById(
            @PathVariable("userId") Long userId
    ) {
        User findUser = userService.getUserById(userId);

        log.info("Get request for search User with id = {}", userId);
        return ResponseEntity.ok(converter.toUserDto(findUser));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(
            @PathVariable("userId") Long userId
    ) {
        userService.deleteUser(userId);

        log.info("Get request for delete User with id = {}", userId);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserDto> updateUser(
            @PathVariable("userId") Long userId,
            @RequestBody UserDto userDto) {

        var user = userService.updateUser(userId, converter.toUserObject(userDto));

        log.info("Get request for update User with id = {}", userId);
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(converter.toUserDto(user));
    }

    @GetMapping
    public List<UserDto> getAllUsers() {
        log.info("Get request to show all users");
        return userService.getAllUser().stream().map(converter::toUserDto).toList();
    }
}

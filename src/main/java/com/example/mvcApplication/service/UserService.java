package com.example.mvcApplication.service;

import com.example.mvcApplication.entity.User;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class UserService {
    AtomicLong idCounter;
    private final Map<Long, User> userMap;

    public UserService() {
        this.userMap = new ConcurrentHashMap<>();
        this.idCounter = new AtomicLong();
    }

    public User createUser(User userToCreate) {
        if (userToCreate.pets() != null && !userToCreate.pets().isEmpty())
            throw new IllegalArgumentException("List of pets must be empty");

        User newUser = new User(
                idCounter.incrementAndGet(),
                userToCreate.name(),
                userToCreate.email(),
                userToCreate.age(),
                new ArrayList<>()
        );
        userMap.put(newUser.id(), newUser);
        return newUser;
    }

    public User getUserById(Long id) {
        return Optional.ofNullable(userMap.get(id))
                .orElseThrow(() -> new NoSuchElementException(
                        "No found user by id = %s".formatted(id)
                ));
    }

    public void deleteUser(Long id) {
        var result = userMap.remove(id);
        if (result == null) {
            throw new NoSuchElementException("No found user by id = %s".formatted(id));
        }
    }

    public User updateUser(Long id, User userToUpdate) {
        if (userMap.get(id) == null) {
            throw new NoSuchElementException("No found user by id = %s".formatted(id));
        }
        var updatedUser = new User(
                id,
                userToUpdate.name(),
                userToUpdate.email(),
                userToUpdate.age(),
                userToUpdate.pets()
        );
        userMap.put(id, userToUpdate);
        return updatedUser;
    }

    public List<User> getAllUser() {
        return userMap.values().stream().toList();
    }
}

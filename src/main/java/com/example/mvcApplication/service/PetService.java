package com.example.mvcApplication.service;

import com.example.mvcApplication.entity.Pet;
import com.example.mvcApplication.entity.User;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class PetService {

    private final UserService userService;
    private final AtomicLong idCounter;

    public PetService(UserService userService) {
        this.userService = userService;
        this.idCounter = new AtomicLong();
    }

    public Pet createPet(Pet pet) {
        Pet newPet = new Pet(
                idCounter.incrementAndGet(),
                pet.name(),
                pet.userId());

        userService.getUserById(newPet.userId()).pets().add(newPet);
        return newPet;
    }

    public Pet getPetById(Long id) {
        return findPetById(id);
    }

    public void deletePet(Long id) {
        Pet petForDelete = findPetById(id);
        userService.getUserById(petForDelete.userId()).pets().remove(petForDelete);
    }

    public Pet updatePet(Pet pet) {
        if (pet.id() == null) {
            throw new IllegalArgumentException("Pet id is null");
        }
        Pet foundPet = findPetById(pet.id());
        Pet petToUpdate = new Pet(
                pet.id(),
                pet.name(),
                pet.userId()
        );
        User user = userService.getUserById(pet.userId());
        user.pets().remove(foundPet);
        user.pets().add(petToUpdate);
        return petToUpdate;
    }

    private Pet findPetById(Long id) {
        return userService.getAllUser().stream()
                .flatMap(user -> user.pets().stream())
                .filter(pet -> pet.id().equals(id))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("No pet with id = %s".formatted(id)));
    }
}

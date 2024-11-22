package com.example.mvcApplication.controller;

import com.example.mvcApplication.converter.ObjectToDtoConverter;
import com.example.mvcApplication.dto.PetDto;
import com.example.mvcApplication.entity.Pet;
import com.example.mvcApplication.service.PetService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pets")
public class PetController {
    private final PetService petService;
    private final ObjectToDtoConverter converter;
    private static final Logger log = LoggerFactory.getLogger(UserController.class);


    public PetController(PetService petService, ObjectToDtoConverter converter) {
        this.petService = petService;
        this.converter = converter;
    }

    @PostMapping
    public ResponseEntity<PetDto> createPet(
            @RequestBody PetDto petDto) {
        Pet newPet = petService.createPet(converter.toPetObject(petDto));

        log.info("Get request to create Pet {}", newPet);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(converter.toPetDto(newPet));
    }

    @GetMapping("/{petId}")
    public ResponseEntity<PetDto> findUserById(
            @PathVariable("petId") Long petId
    ) {
        Pet foundPet = petService.getPetById(petId);

        log.info("Get request for search Pet with id = {}", petId);
        return ResponseEntity.ok(converter.toPetDto(foundPet));
    }

    @DeleteMapping("/{petId}")
    public ResponseEntity<Void> deleteUser(
            @PathVariable("petId") Long petId
    ) {
        petService.deletePet(petId);

        log.info("Get request to delete Pet with id = {}", petId);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    @PutMapping("/{petId}")
    public ResponseEntity<PetDto> updateUser(
            @PathVariable("petId") Long petId,
            @RequestBody PetDto petDto) {
        var pet = petService.updatePet(converter.toPetObject(petDto));

        log.info("Get request to update Pet with id = {}", pet);
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(converter.toPetDto(pet));
    }

}

package com.example.mvcApplication.pet;

import com.example.mvcApplication.dto.PetDto;
import com.example.mvcApplication.entity.Pet;
import com.example.mvcApplication.entity.User;
import com.example.mvcApplication.service.PetService;
import com.example.mvcApplication.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
public class PetServiceTest {

    @Autowired
    private UserService userService;
    @Autowired
    private PetService petsService;
    @Autowired
    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void shouldCreateNewPet() throws Exception {
        var user = userService.createUser(new User(
                null,
                "AlexV",
                "myemail@gmail.com",
                27,
                List.of()
        ));

        var petDto = new PetDto(null, "Lutik", user.id());
        String newPetDto = objectMapper.writeValueAsString(petDto);

        var jsonResponse = mockMvc.perform(post("/pets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newPetDto))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        var petDtoResponse = objectMapper.readValue(jsonResponse, PetDto.class);

        Assertions.assertNotNull(petDtoResponse.id());
        Assertions.assertEquals(petDto.name(), petDtoResponse.name());
        Assertions.assertEquals(petDto.userId(), petDtoResponse.userId());

        Assertions.assertDoesNotThrow(() -> petsService.getPetById(petDtoResponse.id()));

        var userWithPet = userService.getUserById(user.id());
        Assertions.assertEquals(1, userWithPet.pets().size());
        Assertions.assertEquals(petDtoResponse.id(), userWithPet.pets().getFirst().id());
    }

    @Test
    public void shouldDeletePet() throws Exception {
        var user = userService.createUser(new User(
                null,
                "AlexV",
                "myemail@gmail.com",
                27,
                List.of()
        ));
        var pet = petsService.createPet(new Pet(
                null,
                "Lutik",
                user.id()
        ));

        mockMvc.perform(
                        delete("/pets/{id}", pet.id())
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        Assertions.assertEquals(user.pets().size(), 0);
    }
}

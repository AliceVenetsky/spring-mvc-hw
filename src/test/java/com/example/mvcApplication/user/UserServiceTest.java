package com.example.mvcApplication.user;

import com.example.mvcApplication.dto.UserDto;
import com.example.mvcApplication.entity.User;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
public class UserServiceTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserService userService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void shouldSuccessCreateUser() throws Exception {
        var testUserDto = new UserDto(
                null,
                "AlexV",
                "myemail@gmail.com",
                27,
                List.of()
        );

        String userToJson = objectMapper.writeValueAsString(testUserDto);
        String result = mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userToJson)
                ).andExpect(status().is(201))
                .andReturn()
                .getResponse()
                .getContentAsString();

        var responseUserDto = objectMapper.readValue(result, UserDto.class);

        Assertions.assertNotNull(responseUserDto.id());
        Assertions.assertEquals(responseUserDto.name(), testUserDto.name());
        Assertions.assertEquals(responseUserDto.email(), testUserDto.email());
        Assertions.assertEquals(responseUserDto.age(), testUserDto.age());

        Assertions.assertDoesNotThrow(() -> userService.getUserById(responseUserDto.id()));
    }

    @Test
    void shouldSuccessSearchBookById() throws Exception {
        var user = new User(
                null,
                "AlexV",
                "myemail@gmail.com",
                27,
                List.of()
        );
        user = userService.createUser(user);

        String foundUserJson = mockMvc.perform(get("/user/{id}", user.id()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        User foundUser = objectMapper.readValue(foundUserJson, User.class);

        org.assertj.core.api.Assertions.assertThat(user)
                .usingRecursiveComparison()
                .isEqualTo(foundUser);
    }

    @Test
    void shouldReturnNotFoundWhenUserNotPresent() throws Exception {
        mockMvc.perform(get("/users/{id}", Integer.MAX_VALUE))
                .andExpect(status().is(404));
    }

    @Test
    void shouldNotCreateUserWhenRequestNotValid() throws Exception {
        var userDto = new UserDto(
                null,
                null,
                null,
                21,
                List.of()
        );

        String userToJson = objectMapper.writeValueAsString(userDto);
        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userToJson)
        ).andExpect(status().is(400));
    }
}

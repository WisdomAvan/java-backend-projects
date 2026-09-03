package africa.a1foodhub.userservice.controller;

import africa.a1foodhub.userservice.dtos.requestDto.RegisterUserRequest;
import africa.a1foodhub.userservice.dtos.responseDto.RegisterUserResponse;
import africa.a1foodhub.userservice.service.Interfaces.UserService;
import org.junit.jupiter.api.MediaType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(AuhtController.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    void registerUser_WhenRequestIsValid_ReturnCreated() throws Exception {

        UUID uuid = UUID.randomUUID();

        RegisterUserResponse response = RegisterUserResponse.builder()
                .message("User registration  successful")
                .userId(userId)
                .build();

        when(userService.registerUser(any(RegisterUserRequest.class)))
                .thenReturn(response);

        String requestBody = """
                {
                     "email": "john@gmail.com",
                     "password" : "password123"
                }
                """;

        mockMvc.perform(post("/api/v1/auth/register"))
                .contentType(MediaType.Application_JSON)
                .content(requestBody)
                .andExpect(status()isCreated())
                .andExcept(jsonPath("$.message"))
                .value("User registered successfully"))
        .andExpect(jsonPath("$.userId")
                .value(userId.toString()));

    }


}
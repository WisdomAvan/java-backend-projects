package africa.a1foodhub.userservice.controller;

import africa.a1foodhub.userservice.dtos.requestDto.LoginRequest;
import africa.a1foodhub.userservice.dtos.requestDto.RegisterUserRequest;
import africa.a1foodhub.userservice.dtos.responseDto.LoginResponse;
import africa.a1foodhub.userservice.dtos.responseDto.RegisterUserResponse;
import africa.a1foodhub.userservice.exception.InvalidPasswordException;
import africa.a1foodhub.userservice.exception.UserNotFoundException;
import africa.a1foodhub.userservice.service.Interfaces.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @Test
    void registerUser_WhenRequestIsValid_ReturnCreated() throws Exception {

        UUID uuid = UUID.randomUUID();

        RegisterUserResponse response = RegisterUserResponse.builder()
                .message("User registration successful")
                .userId(uuid)
                .build();

        when(userService.registerUser(any(RegisterUserRequest.class)))
                .thenReturn(response);

        String requestBody = """
            {
                "firstName": "John",
                "lastName": "Doe",
                "phoneNumber": "+2348012345678",
                "email": "john@gmail.com",
                "password": "Password123!"
            }
            """;

        mockMvc.perform(
                        post("/api/v1/auth/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message")
                        .value("User registration successful"))
                .andExpect(jsonPath("$.userId")
                        .value(uuid.toString()));
    }


    @Test
    void registerUser_WhenRequestIsInvalid_ReturnBadRequest() throws Exception {

        String requestBody = """
            {
                "firstName": "",
                "lastName": "Doe",
                "phoneNumber": "+2348012345678",
                "email": "invalid-email",
                "password": "weak"
            }
            """;

        mockMvc.perform(
                        post("/api/v1/auth/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    void loginUser_WhenCredentialsAreValid_ReturnOk() throws Exception {

        UUID uuid = UUID.randomUUID();

        LoginResponse response = LoginResponse.builder()
                .message("Login successful")
                .userId(uuid)
                .firstName("John")
                .lastName("Doe")
                .email("john@gmail.com")
                .build();

        when(userService.loginUser(any(LoginRequest.class)))
                .thenReturn(response);

        String requestBody = """
            {
                "email": "john@gmail.com",
                "password": "Password123!"
            }
            """;

        mockMvc.perform(
                        post("/api/v1/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message")
                        .value("Login successful"))
                .andExpect(jsonPath("$.userId")
                        .value(uuid.toString()))
                .andExpect(jsonPath("$.firstName")
                        .value("John"))
                .andExpect(jsonPath("$.lastName")
                        .value("Doe"))
                .andExpect(jsonPath("$.email")
                        .value("john@gmail.com"));
    }

    @Test
    void loginUser_WhenRequestIsInvalid_ReturnBadRequest() throws Exception {

        String requestBody = """
            {
                "email": "invalid-email",
                "password": "comb"
            }    
            """;

        mockMvc.perform(
                        post("/api/v1/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                        )
                .andExpect(status().isBadRequest());
        verify(userService, never())
                .loginUser(any(LoginRequest.class));


    }

    @Test
    void loginUser_WhenRequestIsValid_CallsUserService() throws Exception {

        UUID uuid = UUID.randomUUID();

        LoginResponse response = LoginResponse.builder()
                .message("Login successful")
                .userId(uuid)
                .firstName("John")
                .lastName("Doe")
                .email("john@gmail.com")
                .build();

        when(userService.loginUser(any(LoginRequest.class)))
                .thenReturn(response);

        String requestBody = """
            {
                "email": "john@gmail.com",
                "password": "Password123!"
            }    
            """;

        mockMvc.perform(
                        post("/api/v1/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
                .andExpect(status().isOk());
        verify(userService, times(1)).loginUser(any(LoginRequest.class));


    }

    @Test
    void loginUser_WhenUserDoesNotExist_ReturnNotFound() throws Exception {

        when(userService.loginUser(any(LoginRequest.class)))
                .thenThrow(new UserNotFoundException("User not found"));

        String requestBody = """
            {
                "email": "unknown@gmail.com",
                "password": "Password123!"
            }
            """;

        mockMvc.perform(
                        post("/api/v1/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
                .andExpect(status().isNotFound());
    }

    @Test
    void loginUser_WhenPasswordIsWrong_ReturnUnauthorized() throws Exception {

        when(userService.loginUser(any(LoginRequest.class)))
                .thenThrow(new InvalidPasswordException("Invalid password"));

        String requestBody = """
            {
                "email": "john@gmail.com",
                "password": "WrongPassword123!"
            }
            """;

        mockMvc.perform(
                        post("/api/v1/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
                .andExpect(status().isUnauthorized());
    }

    @Test
    void loginUser_WhenPasswordIsWrong_ReturnErrorMessage() throws Exception {

        when(userService.loginUser(any(LoginRequest.class)))
                .thenThrow(new InvalidPasswordException("Invalid password"));

        String requestBody = """
            {
                "email": "john@gmail.com",
                "password": "WrongPassword123!"
            }
            """;

        mockMvc.perform(
                        post("/api/v1/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Invalid password"));
    }

}
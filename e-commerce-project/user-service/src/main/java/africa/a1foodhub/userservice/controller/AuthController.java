package africa.a1foodhub.userservice.controller;


import africa.a1foodhub.userservice.data.entities.User;
import africa.a1foodhub.userservice.dtos.requestDto.LoginRequest;
import africa.a1foodhub.userservice.dtos.requestDto.RegisterUserRequest;
import africa.a1foodhub.userservice.dtos.responseDto.LoginResponse;
import africa.a1foodhub.userservice.dtos.responseDto.RegisterUserResponse;
import africa.a1foodhub.userservice.service.Interfaces.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<RegisterUserResponse> registerUser(@Valid @RequestBody RegisterUserRequest request) {

        RegisterUserResponse response = userService.registerUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);

    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {

        LoginResponse response = userService.loginUser(request);

        return ResponseEntity.status(HttpStatus.OK).body(response);

    }

}

package africa.datingApp.controllers;

import africa.datingApp.dtos.requestDtos.SeekerLoginRequestDto;
import africa.datingApp.dtos.requestDtos.SeekerLogoutRequestDto;
import africa.datingApp.dtos.requestDtos.SeekerRegistrationRequestDto;
import africa.datingApp.dtos.responseDtos.SeekerLoginResponseDto;
import africa.datingApp.dtos.responseDtos.SeekerLogoutResponseDto;
import africa.datingApp.dtos.responseDtos.SeekerRegistrationResponseDto;
import africa.datingApp.services.OnboardingService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/onboarding")
public class OnboardingController {

    @Autowired
    private OnboardingService onboardingService;

    @PostMapping("/register")
    @Operation(summary ="Register user", description = "Before the user gets to register the following details most be provided correctly")
    public ResponseEntity<SeekerRegistrationResponseDto> register(@Valid @RequestBody SeekerRegistrationRequestDto registerForm){
        return ResponseEntity.status(HttpStatus.CREATED).body(onboardingService.register(registerForm));
//       return ResponseEntity.ok(onboardingService.register(registerForm));
    }

    @PostMapping("/login")
    public ResponseEntity<SeekerLoginResponseDto> login(@Valid @RequestBody SeekerLoginRequestDto loginDetails){
        return ResponseEntity.ok(onboardingService.login(loginDetails));
    }

    @PostMapping("/logout")
    public ResponseEntity <SeekerLogoutResponseDto> logout(@Valid @RequestBody SeekerLogoutRequestDto logoutDetails){
        return ResponseEntity.status(HttpStatus.OK).body(onboardingService.logout(logoutDetails));
    }

}

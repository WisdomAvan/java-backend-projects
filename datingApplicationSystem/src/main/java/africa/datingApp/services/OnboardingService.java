package africa.datingApp.services;

import africa.datingApp.dtos.requestDtos.SeekerLoginRequestDto;
import africa.datingApp.dtos.requestDtos.SeekerLogoutRequestDto;
import africa.datingApp.dtos.requestDtos.SeekerRegistrationRequestDto;
import africa.datingApp.dtos.responseDtos.SeekerLoginResponseDto;
import africa.datingApp.dtos.responseDtos.SeekerLogoutResponseDto;
import africa.datingApp.dtos.responseDtos.SeekerRegistrationResponseDto;

public interface OnboardingService {
    SeekerRegistrationResponseDto register(SeekerRegistrationRequestDto registerForm);
    SeekerLoginResponseDto login(SeekerLoginRequestDto login);
    SeekerLogoutResponseDto logout(SeekerLogoutRequestDto logoutRequestDto);
}

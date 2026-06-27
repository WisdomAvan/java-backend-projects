package africa.datingApp.utils;

import africa.datingApp.data.models.Seeker;
import africa.datingApp.dtos.requestDtos.SeekerRegistrationRequestDto;
import africa.datingApp.dtos.responseDtos.SeekerRegistrationResponseDto;

import java.time.LocalDate;

public class RegisterMapper {

    public static void map(SeekerRegistrationResponseDto response, Seeker seeker) {
        response.setSeekerId(seeker.getSeekerId());
        response.setFirstName(seeker.getFirstName());
        response.setLastName(seeker.getLastName());
        response.setSeekerEmail(seeker.getEmail());
        response.setMessage("Account created successfully");
        response.setCreatedAt(LocalDate.now());
    }

    public static void map(SeekerRegistrationRequestDto registerForm, Seeker seeker) {
        seeker.setFirstName(registerForm.getFirstName());
        seeker.setLastName(registerForm.getLastName());
        seeker.setPhoneNumber(registerForm.getPhoneNumber());
        seeker.setEmail(registerForm.getEmail());
        seeker.setPassword(registerForm.getPassword());
    }
}

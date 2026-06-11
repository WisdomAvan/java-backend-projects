package africa.datingApp.services;

import africa.datingApp.data.models.Seeker;
import africa.datingApp.data.repositories.SeekerRepository;
import africa.datingApp.dtos.requestDtos.SeekerLoginRequestDto;
import africa.datingApp.dtos.requestDtos.SeekerLogoutRequestDto;
import africa.datingApp.dtos.requestDtos.SeekerRegistrationRequestDto;
import africa.datingApp.dtos.responseDtos.SeekerLoginResponseDto;
import africa.datingApp.dtos.responseDtos.SeekerLogoutResponseDto;
import africa.datingApp.dtos.responseDtos.SeekerRegistrationResponseDto;
import africa.datingApp.exceptions.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OnboardingServiceImpl implements OnboardingService {

    private final SeekerRepository seekerRepository;

    @Override
    public SeekerRegistrationResponseDto register(SeekerRegistrationRequestDto registerForm) {

        if (seekerRepository.existsByEmail(registerForm.getEmail())) {
            throw new EmailAlreadyExistsException("Email already exists");
        }
        if(seekerRepository.existsByPhoneNumber(registerForm.getPhoneNumber())){
            throw new PhoneNumberAlreadyExistsException("Phone number already exist");
        }

        Seeker seeker = new Seeker();
        seeker.setFirstName(registerForm.getFirstName());
        seeker.setLastName(registerForm.getLastName());
        seeker.setPhoneNumber(registerForm.getPhoneNumber());
        seeker.setEmail(registerForm.getEmail());
        seeker.setPassword(registerForm.getPassword());

        Seeker savedSeeker = seekerRepository.save(seeker);
        SeekerRegistrationResponseDto response = new SeekerRegistrationResponseDto();
        response.setSeekerId(savedSeeker.getSeekerId());
        response.setFirstName(savedSeeker.getFirstName());
        response.setLastName(savedSeeker.getLastName());
        response.setSeekerEmail(savedSeeker.getEmail());
        response.setActive(String.valueOf(savedSeeker.isActive()));

        return response;
    }

    @Override
    public SeekerLoginResponseDto login(SeekerLoginRequestDto loginDetails){

        Seeker seeker = seekerRepository.findByEmail(loginDetails.getEmail())
                .orElseThrow(() -> new UserNotFoundException("Invalid credentials"));

        if(!loginDetails.getPassword().equals(seeker.getPassword())) {
            throw new InvalidPasswordException("Invalid credentials");
        }

        if(seeker.isLoggedIn()){
            throw new AlreadyLoggedInException("You are already logged in");
        }

        seeker.setLoggedIn(true);
        seekerRepository.save(seeker);

        SeekerLoginResponseDto response = new SeekerLoginResponseDto();
        response.setSeekerId(seeker.getSeekerId());
        response.setFirstName(seeker.getFirstName());
        response.setLastName(seeker.getLastName());
        response.setMessage("Login Successful");

        return response;
    }
    @Override
    public SeekerLogoutResponseDto logout(SeekerLogoutRequestDto logoutDetails){
        Seeker seeker = (Seeker) seekerRepository.findByEmail(logoutDetails.getSeekerEmail()).orElseThrow(()-> new UserNotFoundException("Email not found"));

        if(!seeker.isLoggedIn()){
            throw new AlreadyLoggedOutException("Already logged out");
        }
        seeker.setLoggedIn(false);
        seekerRepository.save(seeker);

        SeekerLogoutResponseDto response = new SeekerLogoutResponseDto();
        response.setMessage("Logout Successful");

        return response;
    }

}

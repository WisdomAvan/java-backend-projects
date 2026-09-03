package africa.a1foodhub.userservice.service.Implementation;

import africa.a1foodhub.userservice.data.entities.Profile;
import africa.a1foodhub.userservice.data.entities.User;
import africa.a1foodhub.userservice.data.repositories.ProfileRepository;
import africa.a1foodhub.userservice.data.repositories.UserRepository;
import africa.a1foodhub.userservice.dtos.requestDto.ProfileRequest;
import africa.a1foodhub.userservice.dtos.responseDto.ProfileResponse;
import africa.a1foodhub.userservice.exception.ProfileNotFoundException;
import africa.a1foodhub.userservice.exception.UserNotFoundException;
import africa.a1foodhub.userservice.exception.ProfileAlreadyExistsException;
import africa.a1foodhub.userservice.service.Interfaces.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;

    @Override
    public ProfileResponse createProfile(UUID userId, ProfileRequest request) {

        User existingUser = userRepository.findById(userId)
                .orElseThrow(() ->
                        new UserNotFoundException("User not found"));

        profileRepository.findByUser(existingUser)
                .ifPresent(profile -> {
                    throw new ProfileAlreadyExistsException(
                            "Profile already exists for this user"
                    );
                });

        Profile profile = Profile.builder()
                .user(existingUser)
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .phoneNumber(request.getPhoneNumber())
                .dob(request.getDob())
                .profilePhotoUrl(request.getProfilePhotoUrl())
                .gender(request.getGender())
                .storeName(request.getStoreName())
                .build();

        Profile savedProfile = profileRepository.save(profile);

        return ProfileResponse.builder()
                .message("Profile created successfully")
                .profileId(savedProfile.getProfileId())
                .userId(savedProfile.getUser().getUserId())
                .build();
    }

    @Override
    public ProfileResponse updateProfile(UUID userId, ProfileRequest request){

        User existingUser = userRepository.findById(userId).orElseThrow(()-> new UserNotFoundException("User not found"));
        Profile existingProfile = profileRepository.findByUser(existingUser).orElseThrow(()-> new ProfileNotFoundException("Profile nor found"));

        existingProfile.setFirstName(request.getFirstName());
        existingProfile.setLastName(request.getLastName());
        existingProfile.setPhoneNumber(request.getPhoneNumber());
        existingProfile.setDob(request.getDob());
        existingProfile.setProfilePhotoUrl(request.getProfilePhotoUrl());
        existingProfile.setGender(request.getGender());
        existingProfile.setStoreName(request.getStoreName());

        Profile updatedProfile = profileRepository.save(existingProfile);

        return ProfileResponse.builder()
                .message("Profile updated successfully")
                .profileId(updatedProfile.getProfileId())
                .userId(updatedProfile.getUser().getUserId())
                .build();
    }

    @Override
    public ProfileResponse getProfile(UUID userId){

        User existingUser = userRepository.findById(userId).orElseThrow(()-> new UserNotFoundException("User not found"));

        Profile existingProfile = profileRepository
                .findByUser(existingUser).orElseThrow(() -> new ProfileNotFoundException("Profile not found"));

        return ProfileResponse.builder()
                .message("Profile retrieved successfully")
                .profileId(existingProfile.getProfileId())
                .userId(existingProfile.getUser().getUserId())
                .build();

    }

    }

package africa.a1foodhub.userservice.service.Implementation;

import africa.a1foodhub.userservice.data.entities.Profile;
import africa.a1foodhub.userservice.data.entities.User;
import africa.a1foodhub.userservice.data.enums.Gender;
import africa.a1foodhub.userservice.data.repositories.ProfileRepository;
import africa.a1foodhub.userservice.data.repositories.UserRepository;
import africa.a1foodhub.userservice.dtos.requestDto.ProfileRequest;
import africa.a1foodhub.userservice.dtos.responseDto.ProfileResponse;
import africa.a1foodhub.userservice.exception.ProfileNotFoundException;
import africa.a1foodhub.userservice.exception.UserNotFoundException;
import africa.a1foodhub.userservice.exception.ProfileAlreadyExistsException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProfileServiceTest {

    @Mock
    private ProfileRepository profileRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ProfileServiceImpl profileService;

    @Test
    void createProfileWithValidDetails_ReturnsSuccessfulResponse() {

        UUID userId = UUID.randomUUID();
        UUID profileId = UUID.randomUUID();

        User existingUser = User.builder()
                .userId(userId)
                .build();

        ProfileRequest request = ProfileRequest.builder()
                .firstName("Tommy")
                .lastName("David")
                .phoneNumber("08012348970")
                .build();

        Profile savedProfile = Profile.builder()
                .profileId(profileId)
                .user(existingUser)
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .phoneNumber(request.getPhoneNumber())
                .build();

        when(userRepository.findById(userId))
                .thenReturn(java.util.Optional.of(existingUser));

        when(profileRepository.save(any(Profile.class)))
                .thenReturn(savedProfile);

        ProfileResponse response =
                profileService.createProfile(userId, request);

        assertNotNull(response);
        assertEquals("Profile created successfully", response.getMessage());
        assertEquals(profileId, response.getProfileId());
        assertEquals(userId, response.getUserId());

        verify(userRepository).findById(userId);
        verify(profileRepository).save(any(Profile.class));
    }

    @Test
    void createProfileWithInvalidUserId_ThrowsUserNotFoundException() {

        UUID userId = UUID.randomUUID();

        ProfileRequest request = ProfileRequest.builder()
                .firstName("Emma")
                .lastName("David")
                .phoneNumber("08012345678")
                .build();

        when(userRepository.findById(userId))
                .thenReturn(java.util.Optional.empty());

        assertThrows(
                UserNotFoundException.class,
                () -> profileService.createProfile(userId, request)
        );

        verify(userRepository).findById(userId);
    }


    @Test
    void createProfileWithValidDetails_WhenUserHasNoProfile_ReturnsSuccessfulResponse() {

        UUID userId = UUID.randomUUID();
        UUID profileId = UUID.randomUUID();

        User existingUser = User.builder()
                .userId(userId)
                .build();

        ProfileRequest request = ProfileRequest.builder()
                .firstName("Emma")
                .lastName("David")
                .phoneNumber("08012345678")
                .build();

        Profile savedProfile = Profile.builder()
                .profileId(profileId)
                .user(existingUser)
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .phoneNumber(request.getPhoneNumber())
                .build();

        when(userRepository.findById(userId))
                .thenReturn(java.util.Optional.of(existingUser));

        when(profileRepository.findByUser(existingUser))
                .thenReturn(java.util.Optional.empty());

        when(profileRepository.save(any(Profile.class)))
                .thenReturn(savedProfile);

        ProfileResponse response =
                profileService.createProfile(userId, request);

        assertNotNull(response);
        assertEquals("Profile created successfully", response.getMessage());
        assertEquals(profileId, response.getProfileId());
        assertEquals(userId, response.getUserId());

        verify(userRepository).findById(userId);
        verify(profileRepository).findByUser(existingUser);
        verify(profileRepository).save(any(Profile.class));
    }

    @Test
    void updateProfileWithInvalidUserId_ThrowsUserNotFoundException() {

        UUID userId = UUID.randomUUID();

        ProfileRequest request = ProfileRequest.builder()
                .firstName("Emma")
                .lastName("David")
                .phoneNumber("08012345678")
                .build();

        when(userRepository.findById(userId))
                .thenReturn(java.util.Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> profileService.updateProfile(userId, request)
        );

        verify(userRepository).findById(userId);
    }

    @Test
    void updateProfileWhenProfileDoesNotExist_ThrowsProfileNotFoundException() {

        UUID userId = UUID.randomUUID();

        User existingUser = User.builder()
                .userId(userId)
                .build();

        ProfileRequest request = ProfileRequest.builder()
                .firstName("Emma")
                .lastName("David")
                .phoneNumber("08012345678")
                .build();

        when(userRepository.findById(userId))
                .thenReturn(java.util.Optional.of(existingUser));

        when(profileRepository.findByUser(existingUser))
                .thenReturn(java.util.Optional.empty());

        assertThrows(
                ProfileNotFoundException.class,
                () -> profileService.updateProfile(userId, request)
        );

        verify(userRepository).findById(userId);
        verify(profileRepository).findByUser(existingUser);
    }

    @Test
    void getProfile_WhereProfileExists_ReturnsProfileResponse() {
        UUID userId = UUID.randomUUID();
        UUID profileId = UUID.randomUUID();

        User existingUser = User.builder()
                .userId(userId)
                .email("wisdom@gmail.com")
                .build();

        Profile existingProfile = Profile.builder()
                .profileId(profileId)
                .user(existingUser)
                .firstName("Wisdom")
                .lastName("Tony")
                .phoneNumber("08012345678")
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));

        when(profileRepository.findByUser(existingUser)).thenReturn(Optional.of(existingProfile));

        ProfileResponse response = profileService.getProfile(userId);

        assertNotNull(response);
        assertEquals(profileId, response.getProfileId());
        assertEquals(userId, response.getUserId());

        verify(userRepository).findById(userId);
        verify(profileRepository).findByUser(existingUser);
    }

    @Test
    void getProfile_WhereProfileDoesNotExist_ThrowsProfileNotFoundException() {

        UUID userId = UUID.randomUUID();

        User existingUser = User.builder()
                .userId(userId)
                .email("wisdom@gmail.com")
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));

        when(profileRepository.findByUser(existingUser)).thenReturn(Optional.empty());

        ProfileNotFoundException exception = assertThrows(
                ProfileNotFoundException.class,
                () -> profileService.getProfile(userId)
        );

        assertEquals("Profile not found", exception.getMessage());

        verify(userRepository).findById(userId);
        verify(profileRepository).findByUser(existingUser);

    }

    @Test
    void getProfile_WhenUserDoesNotExist_ThrowsUserNotFoundException() {
        UUID userId = UUID.randomUUID();

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(
                UserNotFoundException.class,
                () -> profileService.getProfile(userId)
        );

        assertEquals("User not found", exception.getMessage());
        verify(userRepository).findById(userId);
        verify(profileRepository, never()).findByUser(any(User.class));
    }

    @Test
    void getProfile_WhenProfileExists_DoesNotSaveProfile() {

        UUID userId = UUID.randomUUID();
        UUID profileId = UUID.randomUUID();

        User existingUser = User.builder()
                .userId(userId)
                .email("wisdom@gmail.com")
                .build();

        Profile existingProfile = Profile.builder()
                .profileId(profileId)
                .user(existingUser)
                .firstName("Wisdom")
                .lastName("Tony")
                .phoneNumber("08012345678")
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(profileRepository.findByUser(existingUser)).thenReturn(Optional.of(existingProfile));

        ProfileResponse response = profileService.getProfile(userId);

        assertNotNull(response);
        assertEquals(profileId, response.getProfileId());
        assertEquals(userId, response.getUserId());

        verify(profileRepository, never()).save(any(Profile.class));

    }

    @Test
    void createProfile_WhenProfileAlreadyExists_ThrowsProfileAlreadyExistsException() {

        UUID userId = UUID.randomUUID();
        UUID profileId = UUID.randomUUID();

        User existingUser = User.builder()
                .userId(userId)
                .email("wisdom@gmail.com")
                .build();

        Profile existingProfile = Profile.builder()
                .profileId(profileId)
                .user(existingUser)
                .firstName("Wisdom")
                .lastName("Tony")
                .phoneNumber("08012345678")
                .build();

        ProfileRequest request = ProfileRequest.builder()
                .firstName("Wisdom")
                .lastName("Tony")
                .phoneNumber("08098765432")
                .build();


        when(userRepository.findById(userId))
                .thenReturn(Optional.of(existingUser));

        when(profileRepository.findByUser(existingUser))
                .thenReturn(Optional.of(existingProfile));


        ProfileAlreadyExistsException exception = assertThrows(
                ProfileAlreadyExistsException.class,
                () -> profileService.createProfile(userId, request)
        );


        assertEquals(
                "Profile already exists for this user",
                exception.getMessage()
        );

        verify(userRepository).findById(userId);
        verify(profileRepository).findByUser(existingUser);

        verify(profileRepository, never())
                .save(any(Profile.class));
    }

    @Test
    void updateProfile_WhenProfileExists_UpdatesAndReturnsSuccessfulResponse() {

        UUID userId = UUID.randomUUID();
        UUID profileId = UUID.randomUUID();

        User existingUser = User.builder()
                .userId(userId)
                .email("wisdom@gmail.com")
                .build();

        Profile existingProfile = Profile.builder()
                .profileId(profileId)
                .user(existingUser)
                .firstName("OldFirstName")
                .lastName("OldLastName")
                .phoneNumber("08011111111")
                .build();

        ProfileRequest request = ProfileRequest.builder()
                .firstName("NewFirstName")
                .lastName("NewLastName")
                .phoneNumber("08022222222")
                .build();

        Profile updatedProfile = Profile.builder()
                .profileId(profileId)
                .user(existingUser)
                .firstName("NewFirstName")
                .lastName("NewLastName")
                .phoneNumber("08022222222")
                .build();


        when(userRepository.findById(userId))
                .thenReturn(Optional.of(existingUser));

        when(profileRepository.findByUser(existingUser))
                .thenReturn(Optional.of(existingProfile));

        when(profileRepository.save(existingProfile))
                .thenReturn(updatedProfile);


        ProfileResponse response =
                profileService.updateProfile(userId, request);

        assertNotNull(response);
        assertEquals(
                "Profile updated successfully",
                response.getMessage()
        );
        assertEquals(profileId, response.getProfileId());
        assertEquals(userId, response.getUserId());

        assertEquals("NewFirstName", existingProfile.getFirstName());
        assertEquals("NewLastName", existingProfile.getLastName());
        assertEquals("08022222222", existingProfile.getPhoneNumber());

        verify(userRepository).findById(userId);
        verify(profileRepository).findByUser(existingUser);
        verify(profileRepository).save(existingProfile);
    }

    @Test
    void updateProfile_WhenProfileExists_DoesNotChangeProfileIdentity() {

        UUID userId = UUID.randomUUID();
        UUID profileId = UUID.randomUUID();

        User existingUser = User.builder()
                .userId(userId)
                .email("wisdom@gmail.com")
                .build();

        Profile existingProfile = Profile.builder()
                .profileId(profileId)
                .user(existingUser)
                .firstName("Wisdom")
                .lastName("Tony")
                .phoneNumber("08011111111")
                .build();

        ProfileRequest request = ProfileRequest.builder()
                .firstName("NewName")
                .lastName("NewLastName")
                .phoneNumber("08022222222")
                .build();

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(existingUser));

        when(profileRepository.findByUser(existingUser))
                .thenReturn(Optional.of(existingProfile));

        when(profileRepository.save(existingProfile))
                .thenReturn(existingProfile);

        profileService.updateProfile(userId, request);

        assertEquals(profileId, existingProfile.getProfileId());
        assertSame(existingUser, existingProfile.getUser());

        verify(profileRepository).save(existingProfile);
    }

    @Test
    void createProfile_WhenUserHasNoProfile_SavesCorrectProfileData() {

        UUID userId = UUID.randomUUID();

        User existingUser = User.builder()
                .userId(userId)
                .build();

        ProfileRequest profileRequest = ProfileRequest.builder()
                .firstName("John")
                .lastName("Doe")
                .phoneNumber("08012345678")
                .build();

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(existingUser));

        when(profileRepository.findByUser(existingUser))
                .thenReturn(Optional.empty());

        when(profileRepository.save(any(Profile.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        profileService.createProfile(userId, profileRequest);

        ArgumentCaptor<Profile> profileCaptor =
                ArgumentCaptor.forClass(Profile.class);

        verify(profileRepository).save(profileCaptor.capture());

        Profile savedProfile = profileCaptor.getValue();

        assertThat(savedProfile.getUser())
                .isEqualTo(existingUser);

        assertThat(savedProfile.getFirstName())
                .isEqualTo("John");

        assertThat(savedProfile.getLastName())
                .isEqualTo("Doe");

        assertThat(savedProfile.getPhoneNumber())
                .isEqualTo("08012345678");
    }

    @Test
    void updateProfile_WhenProfileExists_UpdatesAllProfileFields() {

        UUID userId = UUID.randomUUID();

        User existingUser = User.builder()
                .userId(userId)
                .build();

        Profile existingProfile = Profile.builder()
                .profileId(UUID.randomUUID())
                .user(existingUser)
                .firstName("OldFirstName")
                .lastName("OldLastName")
                .phoneNumber("08011111111")
                .build();

        ProfileRequest request = ProfileRequest.builder()
                .firstName("NewFirstName")
                .lastName("NewLastName")
                .phoneNumber("08022222222")
                .dob(LocalDate.of(1995, 5, 10))
                .gender(Gender.MALE)
                .profilePhotoUrl("https://example.com/profile.jpg")
                .storeName("A1 Store")
                .build();

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(existingUser));

        when(profileRepository.findByUser(existingUser))
                .thenReturn(Optional.of(existingProfile));

        when(profileRepository.save(existingProfile))
                .thenReturn(existingProfile);

        profileService.updateProfile(userId, request);

        assertThat(existingProfile.getFirstName())
                .isEqualTo("NewFirstName");

        assertThat(existingProfile.getLastName())
                .isEqualTo("NewLastName");

        assertThat(existingProfile.getPhoneNumber())
                .isEqualTo("08022222222");

        assertThat(existingProfile.getDob())
                .isEqualTo(LocalDate.of(1995, 5, 10));

        assertThat(existingProfile.getGender())
                .isEqualTo(Gender.MALE);

        assertThat(existingProfile.getProfilePhotoUrl())
                .isEqualTo("https://example.com/profile.jpg");

        verify(profileRepository).save(existingProfile);
    }

}
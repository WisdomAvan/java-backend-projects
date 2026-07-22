package africa.datingApp.services;

import africa.datingApp.data.repositories.SeekerRepository;
import africa.datingApp.dtos.requestDtos.SeekerLoginRequestDto;
import africa.datingApp.dtos.requestDtos.SeekerLogoutRequestDto;
import africa.datingApp.dtos.requestDtos.SeekerRegistrationRequestDto;
import africa.datingApp.dtos.responseDtos.SeekerLoginResponseDto;
import africa.datingApp.dtos.responseDtos.SeekerLogoutResponseDto;
import africa.datingApp.dtos.responseDtos.SeekerRegistrationResponseDto;
import africa.datingApp.exceptions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;



    @SpringBootTest
    class OnboardingServiceImplTest {

        @Autowired
        private SeekerRepository seekerRepository;
        @Autowired
        private OnboardingService onboardingService;

        @BeforeEach
        void setUp() {

            seekerRepository.deleteAll();
        }

        @Test
        void givenThatRepositoryIsEmpty_WhenSeekerRegister_RepositoryShouldBeSavedByOneSeekerDetail() {
            SeekerRegistrationRequestDto registerDto = new SeekerRegistrationRequestDto();
            registerDto.setFirstName("Comfort");
            registerDto.setLastName("Blessed");
            registerDto.setPhoneNumber("08016161213");
            registerDto.setEmail("comfort@gmail.com");
            registerDto.setPassword("90000000");

            SeekerRegistrationResponseDto savedSeeker = onboardingService.register(registerDto);

            assertThat(savedSeeker).isNotNull();
            assertThat(savedSeeker.getFirstName()).isEqualTo("Comfort");
            assertThat(savedSeeker.getSeekerEmail()).isEqualTo("comfort@gmail.com");
            assertThat(seekerRepository.count()).isEqualTo(1);

        }

        @Test
        void givenThatEmailAlreadyExists_WhenITryToRegisterWithSameEmail_ItShouldThrowException() {

            SeekerRegistrationRequestDto registerDto = new SeekerRegistrationRequestDto();
            registerDto.setFirstName("Comfort");
            registerDto.setLastName("Blessed");
            registerDto.setPhoneNumber("08016161213");
            registerDto.setEmail("comfort@gmail.com");
            registerDto.setPassword("90000000");

            onboardingService.register(registerDto);
            assertThat(seekerRepository.count()).isEqualTo(1);

            SeekerRegistrationRequestDto duplicateRequestDto = new SeekerRegistrationRequestDto();

            registerDto.setFirstName("Tony");
            registerDto.setLastName("Ejiro");
            registerDto.setPhoneNumber("08016161213");
            registerDto.setEmail("comfort@gmail.com");
            registerDto.setPassword("90001234");

            assertThrows(EmailAlreadyExistsException.class, () -> onboardingService.register(registerDto));
        }

        @Test
        void givenThatSeekerRegisterWithAlreadyExistPhoneNumber_ItShouldThrowException() {
            SeekerRegistrationRequestDto registerDto = new SeekerRegistrationRequestDto();
            registerDto.setFirstName("Comfort");
            registerDto.setLastName("Blessed");
            registerDto.setPhoneNumber("08016161213");
            registerDto.setEmail("comfort@gmail.com");
            registerDto.setPassword("90000000");

            onboardingService.register(registerDto);

            SeekerRegistrationRequestDto duplicatePhoneNumber = new SeekerRegistrationRequestDto();
            duplicatePhoneNumber.setFirstName("Bolutife");
            duplicatePhoneNumber.setLastName("Blessing");
            duplicatePhoneNumber.setPhoneNumber("08016161213");
            duplicatePhoneNumber.setEmail("girlforlife@gmail.com");
            duplicatePhoneNumber.setPassword("90000000");


            assertThrows(PhoneNumberAlreadyExistsException.class, () -> onboardingService.register(duplicatePhoneNumber));

        }

        @Test
        void givenRegisteredSeeker_WhenLoginWithValidCredentials_ShouldLoginSuccessfully() {
            SeekerRegistrationRequestDto seekerRegister = new SeekerRegistrationRequestDto();
            seekerRegister.setFirstName("Comfort");
            seekerRegister.setLastName("Blessed");
            seekerRegister.setPhoneNumber("08016161213");
            seekerRegister.setEmail("chichi@gmail.com");
            seekerRegister.setPassword("12345678");

            onboardingService.register(seekerRegister);

            SeekerLoginRequestDto loginRequestDto = new SeekerLoginRequestDto("chichi@gmail.com", "12345678");

            SeekerLoginResponseDto responseDto = onboardingService.login(loginRequestDto);
            assertEquals("Login Successful", responseDto.getMessage());

        }

        @Test
        void givenRegisteredSeekerExists_WhenSeekerLoginWithWrongPassword_ItShouldThrowExpection() {
            SeekerRegistrationRequestDto seekerRegister = new SeekerRegistrationRequestDto();
            seekerRegister.setFirstName("Comfort");
            seekerRegister.setLastName("Blessed");
            seekerRegister.setPhoneNumber("08016161213");
            seekerRegister.setEmail("chichi@gmail.com");
            seekerRegister.setPassword("12345678");

            onboardingService.register(seekerRegister);

            SeekerLoginRequestDto seekerLoginDto = new SeekerLoginRequestDto("chichi@gmail.com", "1234567890");
            assertThrows(InvalidPasswordException.class, () -> onboardingService.login(seekerLoginDto));

        }

        @Test
        void givenSeekerRegisterExistsButAlreadyLoggedIn_WhenLoginAgain_ItShouldThrowException() {
            SeekerRegistrationRequestDto seekerRegister = new SeekerRegistrationRequestDto();
            seekerRegister.setFirstName("Comfort");
            seekerRegister.setLastName("Blessed");
            seekerRegister.setPhoneNumber("08016161213");
            seekerRegister.setEmail("chichi@gmail.com");
            seekerRegister.setPassword("12345678");

            onboardingService.register(seekerRegister);

            SeekerLoginRequestDto loginRequestDto = new SeekerLoginRequestDto("chichi@gmail.com", "12345678");

            SeekerLoginResponseDto responseDto = onboardingService.login(loginRequestDto);
            assertEquals("Login Successful", responseDto.getMessage());

            SeekerLoginRequestDto anotherLoginRequestDto = new SeekerLoginRequestDto("chichi@gmail.com", "12345678");

            assertThrows(AlreadyLoggedInException.class, () -> onboardingService.login(anotherLoginRequestDto));

        }

        @Test
        void givenSeekerIsLoggedIn_WhenLoggedOut_ItShouldShowLoggedOutSuccessful() {

            SeekerRegistrationRequestDto seekerRegister = new SeekerRegistrationRequestDto();
            seekerRegister.setFirstName("Comfort");
            seekerRegister.setLastName("Blessed");
            seekerRegister.setPhoneNumber("08016161213");
            seekerRegister.setEmail("chichi@gmail.com");
            seekerRegister.setPassword("12345678");

            onboardingService.register(seekerRegister);

            SeekerLoginRequestDto loginRequestDto = new SeekerLoginRequestDto("chichi@gmail.com", "12345678");

            SeekerLoginResponseDto responseDto = onboardingService.login(loginRequestDto);
            assertEquals("Login Successful", responseDto.getMessage());

            SeekerLogoutRequestDto logoutRequestDto = new SeekerLogoutRequestDto();
            logoutRequestDto.setSeekerEmail("chichi@gmail.com");

            SeekerLogoutResponseDto logoutResponseDto = onboardingService.logout(logoutRequestDto);
            assertEquals("Logout Successful", logoutResponseDto.getMessage());
        }

        @Test
        void givenSeekerAlreadyLoggedOut_WhenLogoutAgain_ItShouldThrowException() {

            SeekerRegistrationRequestDto seekerRegister = new SeekerRegistrationRequestDto();
            seekerRegister.setFirstName("Comfort");
            seekerRegister.setLastName("Blessed");
            seekerRegister.setPhoneNumber("08016161213");
            seekerRegister.setEmail("chichi@gmail.com");
            seekerRegister.setPassword("12345678");

            onboardingService.register(seekerRegister);

            SeekerLoginRequestDto loginRequestDto = new SeekerLoginRequestDto("chichi@gmail.com", "12345678");

            SeekerLoginResponseDto responseDto = onboardingService.login(loginRequestDto);
            assertEquals("Login Successful", responseDto.getMessage());

            SeekerLogoutRequestDto logoutRequestDto = new SeekerLogoutRequestDto();
            logoutRequestDto.setSeekerEmail("chichi@gmail.com");

            SeekerLogoutResponseDto logoutResponseDto = onboardingService.logout(logoutRequestDto);
            assertEquals("Logout Successful", logoutResponseDto.getMessage());

            SeekerLogoutRequestDto anotherLogoutRequestDto = new SeekerLogoutRequestDto();
            anotherLogoutRequestDto.setSeekerEmail("chichi@gmail.com");
            assertThrows(AlreadyLoggedOutException.class, () -> onboardingService.logout(anotherLogoutRequestDto));
        }

        @Test
        void givenThatSeekerDoesNotExits_WhenSeekerLogout_ItShouldThrowException() {

            SeekerRegistrationRequestDto seekerRegister = new SeekerRegistrationRequestDto();
            seekerRegister.setFirstName("Comfort");
            seekerRegister.setLastName("Blessed");
            seekerRegister.setPhoneNumber("08016161213");
            seekerRegister.setEmail("chichi@gmail.com");
            seekerRegister.setPassword("12345678");

            onboardingService.register(seekerRegister);

            SeekerLoginRequestDto loginRequestDto = new SeekerLoginRequestDto("chichi@gmail.com", "12345678");

            SeekerLoginResponseDto responseDto = onboardingService.login(loginRequestDto);
            assertEquals("Login Successful", responseDto.getMessage());

            SeekerLogoutRequestDto logoutRequestDto = new SeekerLogoutRequestDto();
            logoutRequestDto.setSeekerEmail("babatunde@gmail.com");

            assertThrows(UserNotFoundException.class, () -> onboardingService.logout(logoutRequestDto));

        }

    }


package africa.a1foodhub.userservice.service.Implementation;

import africa.a1foodhub.userservice.data.enums.Role;
import africa.a1foodhub.userservice.data.repositories.UserRepository;
import africa.a1foodhub.userservice.dtos.requestDto.LoginRequest;
import africa.a1foodhub.userservice.dtos.requestDto.RegisterUserRequest;
import africa.a1foodhub.userservice.dtos.responseDto.LoginResponse;
import africa.a1foodhub.userservice.dtos.responseDto.RegisterUserResponse;
import africa.a1foodhub.userservice.data.entities.User;
import africa.a1foodhub.userservice.exception.EmailAlreadyExistsException;
import africa.a1foodhub.userservice.exception.InvalidPasswordException;
import africa.a1foodhub.userservice.exception.PhoneNumberAlreadyExistsException;
import africa.a1foodhub.userservice.exception.UserNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void registerUser_ShouldRegisterSuccessfully() {

        RegisterUserRequest request = RegisterUserRequest.builder()
                .firstName("Timothy")
                .lastName("Anthony")
                .email("wisdom@gmail.com")
                .phoneNumber("+2348012345678")
                .password("Password@123")
                .build();

        when(userRepository.existsByEmail(request.getEmail())).thenReturn(false);
        when(userRepository.existsByPhoneNumber(request.getPhoneNumber())).thenReturn(false);
        when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedPassword");

        User savedUser = User.builder()
                .userId(java.util.UUID.randomUUID())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .passwordHash("encodedPassword")
                .build();

        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        RegisterUserResponse response = userService.registerUser(request);

        assertNotNull(response);
        assertEquals(savedUser.getUserId(), response.getUserId());
        assertEquals("Registration successful", response.getMessage());

        verify(userRepository).save(any(User.class));
    }

    @Test
    void registerUser_ShouldThrowException_WhenEmailIsNull(){

        RegisterUserRequest request = RegisterUserRequest.builder()
                .firstName("Lawal")
                .lastName("Femi")
                .email("wisdom@gmail.com")
                .phoneNumber("+2348012345678")
                .password("Password@123")
                .build();

        when(userRepository.existsByEmail(request.getEmail())).thenReturn(true);

        EmailAlreadyExistsException exception = assertThrows(EmailAlreadyExistsException.class, () -> userService.registerUser(request));

        assertEquals("Email already exists.", exception.getMessage());
        verify(userRepository,never()).save(any(User.class));
    }

    @Test
    void registerUser_ShouldThrowException_WhenPhoneNumberAlreadyExists(){

        RegisterUserRequest request = RegisterUserRequest.builder()
                .firstName("Tolani")
                .lastName("Goodness")
                .email("tolani@gmail.com")
                .phoneNumber("+2348012345678")
                .password("Password@123")
                .build();

        when(userRepository.existsByEmail(request.getEmail())).thenReturn(false);
        when(userRepository.existsByPhoneNumber(request.getPhoneNumber())).thenReturn(true);

        PhoneNumberAlreadyExistsException exception = assertThrows(PhoneNumberAlreadyExistsException.class, () -> userService.registerUser(request));

        assertEquals("Phone number already exists.", exception.getMessage());
        verify(userRepository,never()).save(any(User.class));

    }

    @Test
    void registerUser_ShouldEncodePasswordBeforeSaving(){

        RegisterUserRequest request = RegisterUserRequest.builder()
                .firstName("Wisdom")
                .lastName("Anthony")
                .email("wisdom@gmail.com")
                .phoneNumber("+2348012345678")
                .password("Password@123")
                .build();

        when(userRepository.existsByEmail(request.getEmail())).thenReturn(false);

        when(userRepository.existsByPhoneNumber(request.getPhoneNumber())).thenReturn(false);

        when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedPassword");

        User savedUser = User.builder()
                .userId(UUID.randomUUID())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .passwordHash("encodedPassword")
                .build();

        when(userRepository.save(any(User.class)))
                .thenReturn(savedUser);

        userService.registerUser(request);

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);

        verify(userRepository).save(captor.capture());

        User capturedUser = captor.getValue();

        assertEquals("encodedPassword", capturedUser.getPasswordHash());

        verify(passwordEncoder).encode(request.getPassword());

    }

    @Test
    void registerUser_withExistingEmail_throwsException() {

        RegisterUserRequest request = RegisterUserRequest.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john@gmail.com")
                .phoneNumber("08012345678")
                .password("Password@123")
                .build();

        when(userRepository.existsByEmail(request.getEmail())).thenReturn(true);

        assertThrows(EmailAlreadyExistsException.class, () -> userService.registerUser(request)
        );

        verify(userRepository, never()).save(any(User.class));
    }
    @Test
    void registerUser_withExistingPhoneNumber_throwsException() {

        RegisterUserRequest request = RegisterUserRequest.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john@gmail.com")
                .phoneNumber("08012345678")
                .password("Password@123")
                .build();

        when(userRepository.existsByEmail(request.getEmail())).thenReturn(false);

        when(userRepository.existsByPhoneNumber(request.getPhoneNumber())).thenReturn(true);

        assertThrows(PhoneNumberAlreadyExistsException.class, () -> userService.registerUser(request)
        );

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void registerUserWithExistingEmailThrowsException() {

        RegisterUserRequest request = RegisterUserRequest.builder()
                .firstName("Wisdom")
                .lastName("Anthony")
                .email("wisdom@gmail.com")
                .phoneNumber("+2348012345678")
                .password("Password@123")
                .build();

        when(userRepository.existsByEmail(request.getEmail())).thenReturn(true);

        assertThrows(EmailAlreadyExistsException.class, () -> userService.registerUser(request)
        );

        verify(userRepository).existsByEmail(request.getEmail());

        verify(userRepository, never()).save(any(User.class));

        verify(passwordEncoder, never()).encode(anyString());
    }

    @Test
    void registerUserWithExistingPhoneNumberThrowsException() {

        RegisterUserRequest request = RegisterUserRequest.builder()
                .firstName("Wisdom")
                .lastName("Anthony")
                .email("wisdom@gmail.com")
                .phoneNumber("+2348012345678")
                .password("Password@123")
                .build();

        when(userRepository.existsByEmail(request.getEmail())).thenReturn(false);

        when(userRepository.existsByPhoneNumber(request.getPhoneNumber())).thenReturn(true);

        assertThrows(PhoneNumberAlreadyExistsException.class, () -> userService.registerUser(request)
        );

        verify(userRepository).existsByEmail(request.getEmail());

        verify(userRepository).existsByPhoneNumber(request.getPhoneNumber());

        verify(userRepository, never()).save(any(User.class));

        verify(passwordEncoder, never()).encode(anyString());
    }

    @Test
    void registerUser_ShouldAssignCustomerRole() {

        RegisterUserRequest request = RegisterUserRequest.builder()
                .firstName("Wisdom")
                .lastName("Anthony")
                .email("wisdom@gmail.com")
                .phoneNumber("+2348012345678")
                .password("Password@123")
                .build();

        when(userRepository.existsByEmail(request.getEmail())).thenReturn(false);
        when(userRepository.existsByPhoneNumber(request.getPhoneNumber())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

        User savedUser = User.builder()
                .userId(UUID.randomUUID())
                .role(Role.CUSTOMER)
                .build();

        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        userService.registerUser(request);

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);

        verify(userRepository).save(captor.capture());

        User capturedUser = captor.getValue();

        assertEquals(Role.CUSTOMER, capturedUser.getRole());
    }

    @Test
    void loginUserWithCorrectEmailAndPassword_ReturnsSuccessful() {

        LoginRequest loginDetails = LoginRequest.builder()
                .email("wisdom@gmail.com")
                .password("password@123")
                .build();

        User existingUser = User.builder()
                .userId(UUID.randomUUID())
                .firstName("Wisdom")
                .lastName("Tony")
                .email("wisdom@gmail.com")
                .passwordHash("encodedPassword")
                .role(Role.CUSTOMER)
                .build();

        when(userRepository.findByEmail(loginDetails.getEmail()))
                .thenReturn(Optional.of(existingUser));

        when(passwordEncoder.matches(
                loginDetails.getPassword(),
                existingUser.getPasswordHash()))
                .thenReturn(true);

        LoginResponse loginResponse = userService.loginUser(loginDetails);

        assertNotNull(loginResponse);
        assertEquals("Login successful", loginResponse.getMessage());
        assertEquals(existingUser.getEmail(), loginResponse.getEmail());

        verify(userRepository).findByEmail(loginDetails.getEmail());

        verify(passwordEncoder).matches(
                loginDetails.getPassword(),
                existingUser.getPasswordHash());
    }

    @Test
    void loginUser_WithNonExistenEmail_ThrowUserNotFoundException() {

        LoginRequest loginRequest = LoginRequest.builder()
                .email("unknown@gmail.com")
                .password("password@123")
                .build();

        when(userRepository.findByEmail(loginRequest.getEmail())).thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(UserNotFoundException.class,
                () -> userService.loginUser(loginRequest)
        );

        assertEquals("User not found.", exception.getMessage());

        verify(userRepository).findByEmail(loginRequest.getEmail());

        verify(passwordEncoder, never()).matches(anyString(), anyString());
    }

    @Test
    void loginUser_WithIncorrectPassword_ThrowInvalidPasswordException() {

        LoginRequest loginRequest = LoginRequest.builder()
                .email("wisdom@gmail.com")
                .password("wrongPassword")
                .build();

        User existingUser = User.builder()
                .firstName("Wisdom")
                .lastName("Tony")
                .email("wisdom@gmail.com")
                .passwordHash("encodedPassword")
                .role(Role.CUSTOMER)
                .build();

        when(userRepository.findByEmail(loginRequest.getEmail()))
                .thenReturn(Optional.of(existingUser));

        when(passwordEncoder.matches(
                loginRequest.getPassword(),
                existingUser.getPasswordHash()))
                .thenReturn(false);

        InvalidPasswordException exception = assertThrows(InvalidPasswordException.class, () -> userService.loginUser(loginRequest));

        assertEquals( "invalid password / password not found", exception.getMessage());

        verify(userRepository).findByEmail(loginRequest.getEmail());

        verify(passwordEncoder).matches(
                loginRequest.getPassword(),
                existingUser.getPasswordHash()
        );


    }

//    @BeforeEach
//    void setUp() {
//
//    }
}
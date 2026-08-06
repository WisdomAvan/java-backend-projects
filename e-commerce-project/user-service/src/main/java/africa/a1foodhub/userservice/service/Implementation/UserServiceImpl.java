package africa.a1foodhub.userservice.service.Implementation;

import africa.a1foodhub.userservice.data.entities.User;
import africa.a1foodhub.userservice.data.enums.AccountStatus;
import africa.a1foodhub.userservice.data.enums.Role;
import africa.a1foodhub.userservice.data.repositories.UserRepository;
import africa.a1foodhub.userservice.dtos.requestDto.LoginRequest;
import africa.a1foodhub.userservice.dtos.requestDto.RegisterUserRequest;
import africa.a1foodhub.userservice.dtos.responseDto.LoginResponse;
import africa.a1foodhub.userservice.dtos.responseDto.RegisterUserResponse;
import africa.a1foodhub.userservice.exception.EmailAlreadyExistsException;
import africa.a1foodhub.userservice.exception.InvalidPasswordException;
import africa.a1foodhub.userservice.exception.PhoneNumberAlreadyExistsException;
import africa.a1foodhub.userservice.exception.UserNotFoundException;
import africa.a1foodhub.userservice.service.Interfaces.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    public RegisterUserResponse registerUser(RegisterUserRequest userRequestDetails) {

        if (userRepository.existsByEmail(userRequestDetails.getEmail())) {
            throw new EmailAlreadyExistsException("Email already exists.");
        }

        if (userRepository.existsByPhoneNumber(userRequestDetails.getPhoneNumber())) {
            throw new PhoneNumberAlreadyExistsException("Phone number already exists.");
        }

        User user = User.builder()
                .firstName(userRequestDetails.getFirstName())
                .lastName(userRequestDetails.getLastName())
                .email(userRequestDetails.getEmail())
                .phoneNumber(userRequestDetails.getPhoneNumber())
                .role(Role.CUSTOMER)
                .passwordHash(passwordEncoder.encode(userRequestDetails.getPassword()))
                .build();
        User savedUser = userRepository.save(user);

        RegisterUserResponse response = RegisterUserResponse.builder()
                .userId(savedUser.getUserId())
                .firstName(savedUser.getFirstName())
                .lastName(savedUser.getLastName())
                .email(savedUser.getEmail())
                .message("Registration successful").build();

        return response;
    }

    @Override
    public LoginResponse loginUser(LoginRequest loginDetails) {

        User user = userRepository.findByEmail(loginDetails.getEmail()).orElseThrow(() ->
                new UserNotFoundException("User not found."));

        boolean passwordMatches = passwordEncoder.matches(loginDetails.getPassword(), user.getPasswordHash());

        if (!passwordMatches) {
            throw new InvalidPasswordException("invalid password / password not found");
        }
        LoginResponse response = LoginResponse.builder()
                .message("Login successful")
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .build();
        return response;
    }
}
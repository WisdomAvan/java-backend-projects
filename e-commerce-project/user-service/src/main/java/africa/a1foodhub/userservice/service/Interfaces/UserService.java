package africa.a1foodhub.userservice.service.Interfaces;

import africa.a1foodhub.userservice.dtos.requestDto.LoginRequest;
import africa.a1foodhub.userservice.dtos.requestDto.RegisterUserRequest;
import africa.a1foodhub.userservice.dtos.responseDto.LoginResponse;
import africa.a1foodhub.userservice.dtos.responseDto.RegisterUserResponse;

public interface UserService {

    RegisterUserResponse registerUser(RegisterUserRequest userRequestDetails);
    LoginResponse loginUser(LoginRequest loginDetails);

}

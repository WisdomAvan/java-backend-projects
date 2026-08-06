package africa.a1foodhub.userservice.dtos.responseDto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
public class RegisterUserResponse {

    private UUID userId;
    private String firstName;
    private String lastName;
    private String email;
    private String message;
}

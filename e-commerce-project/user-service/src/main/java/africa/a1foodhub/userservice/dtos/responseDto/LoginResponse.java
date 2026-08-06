package africa.a1foodhub.userservice.dtos.responseDto;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Builder
@Getter
@Setter
public class LoginResponse {

    private String message;
    private UUID userId;
    private String firstName;
    private String lastName;
    private String email;

}

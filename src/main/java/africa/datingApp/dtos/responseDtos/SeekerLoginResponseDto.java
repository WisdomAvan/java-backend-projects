package africa.datingApp.dtos.responseDtos;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@NoArgsConstructor
public class SeekerLoginResponseDto {
    private String seekerId;
    private String firstName;
    private String lastName;
    private LocalDateTime lastSeen;
    private String message;

}

package africa.datingApp.dtos.responseDtos;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class SeekerRegistrationResponseDto {
    private String seekerId;
    private String firstName;
    private String lastName;
    private String seekerEmail;
    private LocalDate createdAt;
    private String message;
    private boolean active = true;
}

package africa.datingApp.dtos.responseDtos;

import lombok.Data;

import java.time.LocalDate;

@Data
public class SeekerRegistrationResponseDto {
    private String seekerId;
    private String friendId;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String seekerEmail;
    private LocalDate createdAt;
    private String message;
    private String active;
}

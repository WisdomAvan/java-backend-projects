package africa.datingApp.dtos.requestDtos;

import africa.datingApp.data.enums.Complexion;
import africa.datingApp.data.enums.Gender;
import africa.datingApp.data.enums.LoveLanguage;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class SeekerProfileRequestDto {
    private String firstName;
    private String lastName;
    private String bio;
    private Gender gender;
    private String location;
    private String PhoneNumber;
    private String seekerPhotoUrl;
    private Complexion complexion;
    private LoveLanguage loveLanguage;
    private double heightInCm;
    private LocalDate dateOfBirth;

    }


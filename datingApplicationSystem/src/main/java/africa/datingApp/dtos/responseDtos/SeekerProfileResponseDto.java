package africa.datingApp.dtos.responseDtos;

import africa.datingApp.data.enums.Complexion;
import africa.datingApp.data.enums.Gender;
import africa.datingApp.data.enums.LoveLanguage;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Data
@NoArgsConstructor
public class SeekerProfileResponseDto {
    private String SeekerId;
    private String firstName;
    private String lastName;
    private Gender gender;
    private LocalDate seekerAge;
    private String location;
    private String seekerPhoto;
    private Complexion complexion;
    private LoveLanguage loveLanguage;
}

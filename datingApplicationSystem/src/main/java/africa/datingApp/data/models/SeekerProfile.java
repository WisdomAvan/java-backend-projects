package africa.datingApp.data.models;

import africa.datingApp.data.enums.BodyType;
import africa.datingApp.data.enums.Complexion;
import africa.datingApp.data.enums.Gender;
import africa.datingApp.data.enums.LoveLanguage;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Data
@Document
@NoArgsConstructor
public class SeekerProfile {

    @Id
    private String profileId;
    private String seekerId;
    private String firstName;
    private String lastName;
    private String bio;
    private Gender gender;
    private double heightInCm;
    private Complexion complexion;
    private String location;
    private LocalDate dateOfBirth;
    private BodyType bodyType;
    private LoveLanguage loveLanguage;
    private String SeekerPhotoUrl;
}

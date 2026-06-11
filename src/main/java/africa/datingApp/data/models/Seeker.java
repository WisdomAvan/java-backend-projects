package africa.datingApp.data.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Data
@Document
@NoArgsConstructor
public class Seeker {

    @Id
    private String seekerId;
    private String profileId;
    private String requestId;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String password;
    private boolean verified;
    private boolean active;
    private boolean loggedIn;
    private LocalDate createdDate;
    private LocalDate lastSeen;
    private String message;


}

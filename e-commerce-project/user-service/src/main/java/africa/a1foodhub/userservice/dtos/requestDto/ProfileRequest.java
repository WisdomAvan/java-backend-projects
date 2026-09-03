package africa.a1foodhub.userservice.dtos.requestDto;

import africa.a1foodhub.userservice.data.entities.Address;
import africa.a1foodhub.userservice.data.enums.Gender;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;





@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfileRequest {

    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @NotBlank
    @Size(min = 11, max = 11)
    private String phoneNumber;
    private LocalDate dob;
    private String profilePhotoUrl;
    private Gender gender;
    private Address address;
    private String storeName;

}

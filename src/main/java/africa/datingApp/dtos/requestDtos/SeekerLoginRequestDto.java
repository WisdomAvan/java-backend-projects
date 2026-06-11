package africa.datingApp.dtos.requestDtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.intellij.lang.annotations.Pattern;

@Data
@AllArgsConstructor
public class SeekerLoginRequestDto {
//    @NotBlank
//    private String firstName;
//    @NotBlank
//    private String lastName;
//    @NotBlank @Pattern(regex = "^\\d{11}$", message = "Phone must be 11 digits")
//    private String phoneNumber;
    @NotBlank @Email
    private String email;
    @NotBlank @Size(min = 8, max = 16, message = "Password character must be atleast 8 characters")
    private String password;


}

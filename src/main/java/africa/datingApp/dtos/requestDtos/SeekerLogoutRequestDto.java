package africa.datingApp.dtos.requestDtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SeekerLogoutRequestDto {
    @Email @NotBlank(message= "Email is required")
    private String seekerEmail;
}

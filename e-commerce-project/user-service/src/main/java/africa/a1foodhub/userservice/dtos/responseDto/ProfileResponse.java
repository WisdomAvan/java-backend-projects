package africa.a1foodhub.userservice.dtos.responseDto;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfileResponse {

    private String message;
    private UUID profileId;
    private UUID userId;
}
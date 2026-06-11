package africa.datingApp.dtos.responseDtos;

import africa.datingApp.data.enums.FriendRequestStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class DeclineFriendResponseDto {
    private String requestId;
    private String seekerId;
    private String seekerUserName;
    private String seekerProfilePhotoUrl;

    private String friendId;
    private String friendUserName;
    private FriendRequestStatus status;
    private LocalDateTime createdDate;
    private LocalDateTime respondTime;



}

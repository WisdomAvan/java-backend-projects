package africa.datingApp.dtos.responseDtos;

import africa.datingApp.data.enums.FriendRequestStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@NoArgsConstructor
public class SendFriendRequestResponseDto {
    private String seekerId;
    private String friendId;
    private String requestId;
    private FriendRequestStatus status;
    private String message;
    private LocalDateTime createdTime;
    private LocalDateTime respondTime;


}

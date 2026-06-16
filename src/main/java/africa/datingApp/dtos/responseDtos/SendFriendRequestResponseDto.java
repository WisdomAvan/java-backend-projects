package africa.datingApp.dtos.responseDtos;

import africa.datingApp.data.enums.FriendRequestStatus;
import lombok.Data;

import java.time.LocalDateTime;
@Data
public class SendFriendRequestResponseDto {
    private String seekerId;
    private String friendId;
    private String requestId;
    private FriendRequestStatus status;
    private String message;
    private LocalDateTime createdTime;
    private LocalDateTime respondTime;



}

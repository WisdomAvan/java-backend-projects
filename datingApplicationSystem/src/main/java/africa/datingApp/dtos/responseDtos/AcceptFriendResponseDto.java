package africa.datingApp.dtos.responseDtos;

import africa.datingApp.data.enums.FriendRequestStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class AcceptFriendResponseDto {
    private String requestId;
    private String seekerId;
    private String friendId;
    private String message;
    private FriendRequestStatus status;
    private LocalDateTime createdTime;
    private LocalDateTime acceptedTime;
}

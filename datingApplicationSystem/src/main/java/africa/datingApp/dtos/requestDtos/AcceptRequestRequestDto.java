package africa.datingApp.dtos.requestDtos;

import africa.datingApp.data.enums.FriendRequestStatus;
import lombok.Data;

@Data
public class AcceptRequestRequestDto {
    private String requestId;
    private String friendId;
    private String seekerId;
    private boolean loggedIn;
    private String message;

}

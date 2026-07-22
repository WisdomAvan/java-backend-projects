package africa.datingApp.dtos.requestDtos;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SendFriendRequestRequestDto {

    private String requestId;

    private String seekerId;

    private String friendId;

    private String profileId;

    private boolean loggedIn;

    private String message;


}

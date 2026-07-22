package africa.datingApp.dtos.requestDtos;

import africa.datingApp.data.enums.FriendRequestStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FriendRequestDto {
    private String requestId;
    private String SeekerId;
    private String friendId;
    private String profileId;
    private String message;
    private FriendRequestStatus status;
}

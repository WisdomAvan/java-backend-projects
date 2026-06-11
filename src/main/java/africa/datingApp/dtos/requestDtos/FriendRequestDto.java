package africa.datingApp.dtos.requestDtos;

import africa.datingApp.data.enums.FriendRequestStatus;

public class FriendRequestDto {
    private String requestId;
    private String SeekerId;
    private String friendId;
    private String profileId;
    private String message;
    private FriendRequestStatus status;
}

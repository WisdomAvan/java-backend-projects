package africa.datingApp.dtos.requestDtos;

import lombok.Data;

@Data
public class DeclineFriendRequestDto {
    private String requestId;
    private String friendId;

}

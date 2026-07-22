package africa.datingApp.services;

import africa.datingApp.dtos.requestDtos.AcceptRequestRequestDto;
import africa.datingApp.dtos.requestDtos.DeclineFriendRequestDto;
import africa.datingApp.dtos.requestDtos.SendFriendRequestRequestDto;
import africa.datingApp.dtos.responseDtos.AcceptFriendResponseDto;
import africa.datingApp.dtos.responseDtos.DeclineFriendResponseDto;
import africa.datingApp.dtos.responseDtos.SendFriendRequestResponseDto;

import java.util.List;

public interface FriendRequestService {

    SendFriendRequestResponseDto sendFriendRequest(SendFriendRequestRequestDto requestDto);
    AcceptFriendResponseDto acceptRequest(AcceptRequestRequestDto acceptRequest);
    DeclineFriendResponseDto declineRequest(DeclineFriendRequestDto declineRequestDto);

    List< SendFriendRequestResponseDto> viewAllSentRequest(SendFriendRequestRequestDto sentRequest);
    List < AcceptFriendResponseDto > viewAllAcceptedRequest(AcceptRequestRequestDto acceptedRequest);


}

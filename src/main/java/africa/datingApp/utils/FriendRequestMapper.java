package africa.datingApp.utils;

import africa.datingApp.data.enums.FriendRequestStatus;
import africa.datingApp.data.models.FriendRequest;
import africa.datingApp.dtos.requestDtos.AcceptRequestRequestDto;
import africa.datingApp.dtos.requestDtos.DeclineFriendRequestDto;
import africa.datingApp.dtos.requestDtos.SendFriendRequestRequestDto;
import africa.datingApp.dtos.responseDtos.AcceptFriendResponseDto;
import africa.datingApp.dtos.responseDtos.DeclineFriendResponseDto;
import africa.datingApp.dtos.responseDtos.SendFriendRequestResponseDto;

import java.time.LocalDateTime;

public class FriendRequestMapper {
    public SendFriendRequestResponseDto toResponse(FriendRequest request) {
        SendFriendRequestResponseDto response = new SendFriendRequestResponseDto();
        response.setSeekerId(request.getSeekerId());
        response.setFriendId(request.getFriendId());
        response.setMessage(request.getMessage());
        response.setStatus(request.getStatus());
        response.setCreatedTime(request.getCreatedTime());
        response.setRespondTime(request.getRespondTime());

        return response;
    }

    public FriendRequest toEntity(SendFriendRequestRequestDto requestDto){
        FriendRequest friendRequest = new FriendRequest();

        friendRequest.setRequestId(requestDto.getRequestId());
        friendRequest.setSeekerId(requestDto.getSeekerId());
        friendRequest.setFriendId(requestDto.getFriendId());
        friendRequest.setProfileId(requestDto.getProfileId());
        friendRequest.setStatus(FriendRequestStatus.PENDING);
        friendRequest.setCreatedTime(LocalDateTime.now());

        return friendRequest;

    }

    public AcceptFriendResponseDto toResponseDto(FriendRequest request){
        AcceptFriendResponseDto acceptRequest = new AcceptFriendResponseDto();

        acceptRequest.setSeekerId(request.getSeekerId());
        acceptRequest.setFriendId(request.getFriendId());
        acceptRequest.setStatus(FriendRequestStatus.ACCEPTED);
        acceptRequest.setAcceptedTime(LocalDateTime.now());
        acceptRequest.setMessage(request.getMessage());

        return acceptRequest;

    }

    public FriendRequest toModel(AcceptRequestRequestDto request){
        FriendRequest friendRequest = new FriendRequest();
        friendRequest.setSeekerId(request.getSeekerId());
        friendRequest.setRespondTime(LocalDateTime.now());

        return friendRequest;
    }

    public DeclineFriendResponseDto toDeclineFriendResponseDto(FriendRequest request){
        DeclineFriendResponseDto declineRequest = new DeclineFriendResponseDto();
        declineRequest.setFriendId(request.getFriendId());
        declineRequest.setSeekerId(request.getSeekerId());
        declineRequest.setStatus(FriendRequestStatus.DECLINED);
        declineRequest.setRespondTime(request.getRespondTime());

        return declineRequest;
    }

    public FriendRequest toUpdateModel(DeclineFriendRequestDto declineRequest){
        FriendRequest friendRequest = new FriendRequest();
        friendRequest.setRequestId(declineRequest.getRequestId());
        friendRequest.setFriendId(declineRequest.getFriendId());
        friendRequest.setRespondTime(LocalDateTime.now());

        return friendRequest;
    }
}

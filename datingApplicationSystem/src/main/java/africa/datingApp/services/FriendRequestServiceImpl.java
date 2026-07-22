package africa.datingApp.services;

import africa.datingApp.data.enums.FriendRequestStatus;
import africa.datingApp.data.models.FriendRequest;
import africa.datingApp.data.models.Seeker;
import africa.datingApp.data.repositories.FriendRequestRepository;
import africa.datingApp.data.repositories.SeekerRepository;
import africa.datingApp.dtos.requestDtos.AcceptRequestRequestDto;
import africa.datingApp.dtos.requestDtos.DeclineFriendRequestDto;
import africa.datingApp.dtos.requestDtos.SendFriendRequestRequestDto;
import africa.datingApp.dtos.responseDtos.AcceptFriendResponseDto;
import africa.datingApp.dtos.responseDtos.DeclineFriendResponseDto;
import africa.datingApp.dtos.responseDtos.SendFriendRequestResponseDto;
import africa.datingApp.exceptions.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FriendRequestServiceImpl implements FriendRequestService {
    private final SeekerRepository seekerRepository;
    private final FriendRequestRepository friendRequestRepository;


    @Override
    public SendFriendRequestResponseDto sendFriendRequest(SendFriendRequestRequestDto requestDto) {
        if (!requestDto.isLoggedIn()) {
            throw new UserNotLoggedInException("Login first");
        }

        if (requestDto.getSeekerId().equals(requestDto.getFriendId())) {
            throw new CannotSendRequestToSelfException("You cannot send a friend to yourself");
        }

        seekerRepository.findById(requestDto.getSeekerId())
                .orElseThrow(() -> new UserNotFoundException("Sender not found"));

        seekerRepository.findById(requestDto.getFriendId())
                .orElseThrow(() -> new UserNotFoundException("Friend does not exist"));

        FriendRequest friendRequest = new FriendRequest();
        friendRequest.setSeekerId(requestDto.getSeekerId());
        friendRequest.setFriendId(requestDto.getFriendId());
        friendRequest.setMessage(requestDto.getMessage());
        friendRequest.setCreatedTime(LocalDateTime.now());
        friendRequest.setStatus(FriendRequestStatus.PENDING);

        FriendRequest savedRequest = friendRequestRepository.save(friendRequest);

        SendFriendRequestResponseDto response = new SendFriendRequestResponseDto();
        response.setRequestId(savedRequest.getRequestId());
        response.setSeekerId(requestDto.getSeekerId());
        response.setFriendId(requestDto.getFriendId());
        response.setMessage("Request Successful");
        response.setStatus(savedRequest.getStatus());

        return response;
    }

    @Override
    public AcceptFriendResponseDto acceptRequest(AcceptRequestRequestDto acceptRequest) {

        FriendRequest friendRequest = friendRequestRepository.findById(acceptRequest.getRequestId())
                .orElseThrow(() -> new FriendRequestNotFoundException("Friend request not found"));

        friendRequest.setStatus(FriendRequestStatus.ACCEPTED);
        friendRequestRepository.save(friendRequest);

        Seeker seeker = seekerRepository.findById(friendRequest.getSeekerId())
                .orElseThrow(() -> new UserNotFoundException("Seeker not found"));

        Seeker friend = seekerRepository.findById(friendRequest.getFriendId())
                .orElseThrow(() -> new UserNotFoundException("Friend not found"));

        if (seeker.getFriends() == null) {
            seeker.setFriends(new ArrayList<>());
        }

        if (friend.getFriends() == null) {
            friend.setFriends(new ArrayList<>());
        }

        seeker.getFriends().add(friend.getSeekerId());
        friend.getFriends().add(seeker.getSeekerId());

        seekerRepository.save(seeker);
        seekerRepository.save(friend);

        AcceptFriendResponseDto response = new AcceptFriendResponseDto();
        response.setSeekerId(friendRequest.getSeekerId());
        response.setFriendId(friendRequest.getFriendId());
        response.setMessage("Request Accepted");
        response.setStatus(friendRequest.getStatus());

        return response;
    }

    @Override
    public  DeclineFriendResponseDto declineRequest(DeclineFriendRequestDto declineRequestDto) {

        FriendRequest friendRequest = friendRequestRepository.findById(declineRequestDto.getRequestId())
                .orElseThrow(() -> new FriendRequestNotFoundException("Friend request not found"));

        if (friendRequest.getStatus() == FriendRequestStatus.DECLINED) {
            throw new FriendRequestAlreadyDeclinedException("Request Already Declined");
        }

        if(friendRequest.getStatus() == FriendRequestStatus.ACCEPTED) {
            throw new InvalidFriendRequestStateException("Request Already Accepted");
        }

        friendRequest.setStatus(FriendRequestStatus.DECLINED);
            seekerRepository.findById(declineRequestDto.getFriendId())
                    .orElseThrow(() -> new UserNotFoundException("Friend doesn't exist"));


            friendRequestRepository.save(friendRequest);

            DeclineFriendResponseDto response = new DeclineFriendResponseDto();
            response.setRequestId(friendRequest.getRequestId());
            response.setSeekerId(friendRequest.getSeekerId());
            response.setFriendId(friendRequest.getFriendId());
            response.setStatus(FriendRequestStatus.DECLINED);
            response.setMessage("Request Declined");

            return response;

    }


    @Override
    public List<SendFriendRequestResponseDto> viewAllSentRequest(SendFriendRequestRequestDto sentRequest) {
        return List.of();
    }

    @Override
    public List<AcceptFriendResponseDto> viewAllAcceptedRequest(AcceptRequestRequestDto acceptedRequest) {
        return List.of();
    }
}



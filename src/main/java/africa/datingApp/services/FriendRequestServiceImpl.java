package africa.datingApp.services;

import africa.datingApp.data.enums.FriendRequestStatus;
import africa.datingApp.data.models.FriendRequest;
import africa.datingApp.data.repositories.FriendRequestRepository;
import africa.datingApp.data.repositories.SeekerRepository;
import africa.datingApp.dtos.requestDtos.AcceptRequestRequestDto;
import africa.datingApp.dtos.requestDtos.DeclineFriendRequestDto;
import africa.datingApp.dtos.requestDtos.SendFriendRequestRequestDto;
import africa.datingApp.dtos.responseDtos.AcceptFriendResponseDto;
import africa.datingApp.dtos.responseDtos.DeclineFriendResponseDto;
import africa.datingApp.dtos.responseDtos.SendFriendRequestResponseDto;
import africa.datingApp.exceptions.CannotSendRequestToSelfException;
import africa.datingApp.exceptions.UserNotFoundException;
import africa.datingApp.exceptions.UserNotLoggedInException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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

        FriendRequest request = new FriendRequest();
        request.setSeekerId(requestDto.getSeekerId());
        request.setFriendId(requestDto.getFriendId());
        request.setMessage(requestDto.getMessage());
        request.setCreatedTime(LocalDateTime.now());
        request.setStatus(FriendRequestStatus.PENDING);

        FriendRequest savedRequest = friendRequestRepository.save(request);

        SendFriendRequestResponseDto response = new SendFriendRequestResponseDto();
        response.setSeekerId(requestDto.getSeekerId());
        response.setFriendId(requestDto.getFriendId());
        response.setMessage("Request Successful");
        response.setStatus(savedRequest.getStatus());

        return response;
    }

    @Override
    public AcceptFriendResponseDto acceptRequest(AcceptRequestRequestDto acceptRequest) {

        FriendRequest request = new FriendRequest();
        request.setSeekerId(acceptRequest.getSeekerId());
        request.setFriendId(acceptRequest.getFriendId());
        request.setMessage(acceptRequest.getMessage());
        request.setCreatedTime(LocalDateTime.now());
        request.setStatus(FriendRequestStatus.ACCEPTED);
        FriendRequest savedRequest = friendRequestRepository.save(request);

        AcceptFriendResponseDto response = new AcceptFriendResponseDto();
        response.setSeekerId(acceptRequest.getSeekerId());
        response.setFriendId(acceptRequest.getFriendId());
        response.setMessage("Request Accepted");
        response.setStatus(savedRequest.getStatus());

        return response;
    }

    @Override
    public  DeclineFriendResponseDto declineRequest(DeclineFriendRequestDto declineRequestDto) {
        return null;
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



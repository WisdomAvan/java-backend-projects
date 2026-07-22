package africa.datingApp.data.repositories;

import africa.datingApp.data.enums.FriendRequestStatus;
import africa.datingApp.data.models.FriendRequest;
import africa.datingApp.dtos.responseDtos.FriendRequestResponseDto;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface FriendRequestRepository extends MongoRepository <FriendRequest, String> {
    Optional<FriendRequest> findBySeekerIdAndFriendId(String seekerId, String friendId);

    List<FriendRequest> findByFriendId(String friendId);

    List <FriendRequest> findByFriendIdAndStatus(String friendId, FriendRequestStatus status);

    List <FriendRequest> findBySeekerIdAndStatus(String seekerId,FriendRequestStatus status);

    boolean existsBySeekerIdAndFriendIdAndStatus(String seekerId, String friendId, FriendRequestStatus status);


}

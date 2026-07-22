package africa.datingApp.data.models;

import africa.datingApp.data.enums.FriendRequestStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document
@NoArgsConstructor
public class FriendRequest {

    @Id
    private String requestId;
    private String seekerId;
    private String friendId;
    private String profileId;
    private String message;
    private FriendRequestStatus status;
    private LocalDateTime createdTime;
    private LocalDateTime respondTime;

}

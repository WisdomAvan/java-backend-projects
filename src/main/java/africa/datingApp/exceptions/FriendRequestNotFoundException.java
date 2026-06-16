package africa.datingApp.exceptions;

public class FriendRequestNotFoundException extends DatingAppException {

    public FriendRequestNotFoundException() {
        super("Friend request not found");
    }

    public FriendRequestNotFoundException(String message) {
        super(message);
    }
}
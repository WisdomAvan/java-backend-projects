package africa.datingApp.exceptions;

public class CannotSendRequestToSelfException extends DatingAppException {
    public CannotSendRequestToSelfException(String message) {
        super(message);
    }
}

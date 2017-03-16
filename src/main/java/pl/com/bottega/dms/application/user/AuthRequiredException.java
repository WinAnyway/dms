package pl.com.bottega.dms.application.user;

public class AuthRequiredException extends RuntimeException {

    public AuthRequiredException(String message) {
        super(message);
    }

    public AuthRequiredException() {

    }
}

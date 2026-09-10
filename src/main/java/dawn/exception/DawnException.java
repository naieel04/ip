package dawn.exception;

/** Reports an expected application error with a user-facing explanation. */
public class DawnException extends Exception {
    public DawnException(String message) {
        super(message);
    }
}

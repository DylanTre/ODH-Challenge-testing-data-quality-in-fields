package it.unibz.exception;

public class NotImplementedException extends RuntimeException {
    public NotImplementedException() {
        super("Method or functionality not yet implemented");
    }

    public NotImplementedException(String message) {
        super(message);
    }
}

package it.unibz.exception;

public class NotImplementedException extends RuntimeException {

    private static final String NOT_YET_IMPLEMENTED_ERROR = "Method or functionality not yet implemented";

    public NotImplementedException() {
        super(NOT_YET_IMPLEMENTED_ERROR);
    }

    public NotImplementedException(String message) {
        super(message);
    }
}

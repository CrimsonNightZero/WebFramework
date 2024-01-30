package org.web.domain.exceptions;

public class InvalidNameFormatException extends RuntimeException {
    public InvalidNameFormatException() {
    }

    public InvalidNameFormatException(String message) {
        super(message);
    }

    public InvalidNameFormatException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidNameFormatException(Throwable cause) {
        super(cause);
    }

    public InvalidNameFormatException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

package org.web.domain.exceptions;

public class IncorrectFormatOfRegistrationException extends RuntimeException{
    public IncorrectFormatOfRegistrationException() {
    }

    public IncorrectFormatOfRegistrationException(String message) {
        super(message);
    }

    public IncorrectFormatOfRegistrationException(String message, Throwable cause) {
        super(message, cause);
    }

    public IncorrectFormatOfRegistrationException(Throwable cause) {
        super(cause);
    }

    public IncorrectFormatOfRegistrationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

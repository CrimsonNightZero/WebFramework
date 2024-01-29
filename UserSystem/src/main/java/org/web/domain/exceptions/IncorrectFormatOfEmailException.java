package org.web.domain.exceptions;

public class IncorrectFormatOfEmailException extends RuntimeException{
    public IncorrectFormatOfEmailException() {
    }

    public IncorrectFormatOfEmailException(String message) {
        super(message);
    }

    public IncorrectFormatOfEmailException(String message, Throwable cause) {
        super(message, cause);
    }

    public IncorrectFormatOfEmailException(Throwable cause) {
        super(cause);
    }

    public IncorrectFormatOfEmailException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

package org.web.domain.exceptions;

public class CredentialsInvalidException extends RuntimeException{
    public CredentialsInvalidException() {
    }

    public CredentialsInvalidException(String message) {
        super(message);
    }

    public CredentialsInvalidException(String message, Throwable cause) {
        super(message, cause);
    }

    public CredentialsInvalidException(Throwable cause) {
        super(cause);
    }

    public CredentialsInvalidException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

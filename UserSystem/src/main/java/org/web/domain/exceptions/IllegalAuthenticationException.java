package org.web.domain.exceptions;

public class IllegalAuthenticationException extends RuntimeException{
    public IllegalAuthenticationException() {
        super();
    }

    public IllegalAuthenticationException(String message) {
        super(message);
    }

    public IllegalAuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalAuthenticationException(Throwable cause) {
        super(cause);
    }

    protected IllegalAuthenticationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

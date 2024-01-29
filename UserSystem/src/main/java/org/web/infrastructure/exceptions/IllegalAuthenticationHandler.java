package org.web.infrastructure.exceptions;

import org.web.domain.core.ExceptionHandler;
import org.web.domain.exceptions.IllegalAuthenticationException;

public class IllegalAuthenticationHandler extends ExceptionHandler<IllegalAuthenticationException> {
    public IllegalAuthenticationHandler() {
        super(IllegalAuthenticationException.class, 401);
    }
}

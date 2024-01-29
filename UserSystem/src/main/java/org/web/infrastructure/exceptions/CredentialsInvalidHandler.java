package org.web.infrastructure.exceptions;

import org.web.domain.core.ExceptionHandler;
import org.web.domain.exceptions.CredentialsInvalidException;

public class CredentialsInvalidHandler extends ExceptionHandler<CredentialsInvalidException> {
    public CredentialsInvalidHandler() {
        super(CredentialsInvalidException.class, 400);
    }
}

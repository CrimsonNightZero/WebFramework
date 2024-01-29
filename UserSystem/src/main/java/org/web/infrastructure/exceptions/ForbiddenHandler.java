package org.web.infrastructure.exceptions;

import org.web.domain.core.ExceptionHandler;
import org.web.domain.exceptions.ForbiddenException;

public class ForbiddenHandler extends ExceptionHandler<ForbiddenException> {
    public ForbiddenHandler() {
        super(ForbiddenException.class, 403);
    }
}

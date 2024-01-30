package org.web.infrastructure.exceptions;

import org.web.domain.core.ExceptionHandler;
import org.web.domain.exceptions.DuplicateEmailException;

public class DuplicateEmailHandler extends ExceptionHandler<DuplicateEmailException> {
    public DuplicateEmailHandler() {
        super(DuplicateEmailException.class, 400);
    }
}

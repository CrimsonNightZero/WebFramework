package org.web.infrastructure.exceptions;

import org.web.domain.core.ExceptionHandler;
import org.web.domain.exceptions.IncorrectFormatOfEmailException;

public class IncorrectFormatOfEmailHandler extends ExceptionHandler<IncorrectFormatOfEmailException> {
    public IncorrectFormatOfEmailHandler() {
        super(IncorrectFormatOfEmailException.class, 400);
    }
}

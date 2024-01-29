package org.web.infrastructure.exceptions;

import org.web.domain.core.ExceptionHandler;
import org.web.domain.exceptions.InvalidNameFormatException;

public class InvalidNameFormatHandler extends ExceptionHandler<InvalidNameFormatException> {
    public InvalidNameFormatHandler() {
        super(InvalidNameFormatException.class, 400);
    }
}

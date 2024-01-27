package org.web.domain.ext.exceptions;

import org.web.domain.core.ExceptionHandler;
import org.web.domain.core.HTTPRequest;

public class NotExpectedExecutionHandler extends ExceptionHandler<RuntimeException> {

    public NotExpectedExecutionHandler() {
        super(RuntimeException.class, 500);
    }

    @Override
    protected String getExceptionMessage(HTTPRequest httpRequest, Throwable throwable) {
        return "The exception is not expected";
    }
}

package org.web.domain.ext.exceptions;

import org.web.domain.core.ExceptionHandler;
import org.web.domain.core.HTTPRequest;
import org.web.infrastructure.exceptions.NotFindPathException;


public class NotFindPathExceptionHandler extends ExceptionHandler<NotFindPathException> {

    public NotFindPathExceptionHandler() {
        super(NotFindPathException.class, 404);
    }

    @Override
    protected String getExceptionMessage(HTTPRequest httpRequest, Throwable throwable) {
        return String.format("Cannot find the path %s", httpRequest.getHttpPath());
    }
}

package org.web.domain.ext.exceptions;

import org.web.domain.core.ExceptionHandler;
import org.web.domain.core.HTTPRequest;
import org.web.domain.core.exceptions.NotAllowedMethodException;

public class NotAllowedMethodExceptionHandler extends ExceptionHandler<NotAllowedMethodException> {

    public NotAllowedMethodExceptionHandler() {
        super(NotAllowedMethodException.class, 405);
    }

    @Override
    protected String getExceptionMessage(HTTPRequest httpRequest, Throwable throwable) {
        return String.format("The method %s is not allowed on %s", httpRequest.getHttpMethod(),
                httpRequest.getHttpPath());
    }
}

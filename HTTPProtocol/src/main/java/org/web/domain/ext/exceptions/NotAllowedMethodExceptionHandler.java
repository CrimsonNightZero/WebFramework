package org.web.domain.ext.exceptions;

import org.web.domain.core.ExceptionHandler;
import org.web.domain.core.HTTPRequest;
import org.web.domain.core.HTTPResponse;
import org.web.infrastructure.exceptions.NotAllowedMethodException;

import java.util.HashMap;
import java.util.Map;

public class NotAllowedMethodExceptionHandler extends ExceptionHandler {

    @Override
    protected boolean matchThrownException(Throwable throwable) {
        return throwable instanceof NotAllowedMethodException;
    }

    @Override
    protected HTTPResponse response(HTTPRequest httpRequest, Throwable throwable) {
        HTTPResponse httpResponse = new HTTPResponse(405);
        Map<String, String> headers = new HashMap<>();
        headers.put("content-type", "plain/text");
        headers.put("content-encoding", "UTF-8");
        httpResponse.setHttpHeaders(headers);
        httpResponse.setBody(String.format("The method %s is not allowed on %s", httpRequest.getHttpMethod(), httpRequest.getHttpPath()));
        return httpResponse;
    }
}

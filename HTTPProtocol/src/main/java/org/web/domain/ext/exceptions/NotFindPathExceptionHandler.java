package org.web.domain.ext.exceptions;

import org.web.domain.core.ExceptionHandler;
import org.web.domain.core.HTTPRequest;
import org.web.domain.core.HTTPResponse;
import org.web.infrastructure.exceptions.NotFindPathException;

import java.util.HashMap;
import java.util.Map;

public class NotFindPathExceptionHandler extends ExceptionHandler {

    @Override
    protected boolean matchThrownException(Throwable throwable) {
        return throwable instanceof NotFindPathException;
    }

    @Override
    protected HTTPResponse response(HTTPRequest httpRequest) {
        HTTPResponse httpResponse = new HTTPResponse(404);
        Map<String, String> headers = new HashMap<>();
        headers.put("content-type", "plain/text");
        headers.put("content-encoding", "UTF-8");
        httpResponse.setHttpHeaders(headers);
        httpResponse.setBody(String.format("Cannot find the path %s", httpRequest.getHttpPath()));
        return httpResponse;
    }
}

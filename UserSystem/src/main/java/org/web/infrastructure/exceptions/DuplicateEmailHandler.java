package org.web.infrastructure.exceptions;

import org.web.domain.core.ExceptionHandler;
import org.web.domain.core.HTTPRequest;
import org.web.domain.core.HTTPResponse;
import org.web.domain.exceptions.DuplicateEmailException;

import java.util.HashMap;
import java.util.Map;

public class DuplicateEmailHandler extends ExceptionHandler {
    @Override
    protected boolean matchThrownException(Throwable throwable) {
        return throwable instanceof DuplicateEmailException;
    }

    @Override
    protected HTTPResponse response(HTTPRequest httpRequest, Throwable throwable) {
        HTTPResponse httpResponse = new HTTPResponse(400);
        Map<String, String> headers = new HashMap<>();
        headers.put("content-type", "plain/text");
        headers.put("content-encoding", "UTF-8");
        httpResponse.setHttpHeaders(headers);
        httpResponse.setBody(throwable.getMessage());
        return httpResponse;
    }
}

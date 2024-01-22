package org.web.domain.ext.exceptions;

import org.web.domain.core.ExceptionHandler;
import org.web.domain.core.HTTPRequest;
import org.web.domain.core.HTTPResponse;

import java.util.HashMap;
import java.util.Map;

public class NotExpectedExecutionHandler extends ExceptionHandler {

    @Override
    protected boolean matchThrownException(Throwable throwable) {
        return true;
    }

    @Override
    protected HTTPResponse response(HTTPRequest httpRequest) {
        HTTPResponse httpResponse = new HTTPResponse(500);
        Map<String, String> headers = new HashMap<>();
        headers.put("content-type", "plain/text");
        headers.put("content-encoding", "UTF-8");
        httpResponse.setHttpHeaders(headers);
        httpResponse.setBody("The exception is not expected");
        return httpResponse;
    }
}

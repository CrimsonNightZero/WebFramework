package org.web.domain.ext;

import org.web.domain.core.HTTPHandler;
import org.web.domain.core.HTTPMethod;
import org.web.domain.core.HTTPRequest;
import org.web.domain.core.HTTPResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PATCHController extends HTTPHandler {
    public PATCHController(HTTPHandler next) {
        super(next);
    }

    @Override
    protected boolean matchHTTPMethod(HTTPMethod httpMethod) {
        return HTTPMethod.PATCH.equals(httpMethod);
    }

    @Override
    protected boolean matchHTTPPath(String httpPath) {
        return "/api/users/1".equals(httpPath);
    }

    @Override
    protected HTTPResponse process(HTTPRequest httpRequest) {
        HTTPResponse httpResponse = new HTTPResponse(200);
        Map<String, String> headers = new HashMap<>();
        headers.put("content-type", "application/json");
        headers.put("content-encoding", "UTF-8");
        httpResponse.setHttpHeaders(headers);

        Map<String, Object> body = new HashMap<>();
        body.put("id", 1);
        body.put("email", "abc@gmail.com");
        body.put("name", "newAbc");
        body.put("password", "hello");
        httpResponse.setResponseBody(body);
        return httpResponse;
    }
}

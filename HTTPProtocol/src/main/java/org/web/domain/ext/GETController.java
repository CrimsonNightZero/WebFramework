package org.web.domain.ext;

import org.web.domain.core.HTTPHandler;
import org.web.domain.core.HTTPMethod;
import org.web.domain.core.HTTPRequest;
import org.web.domain.core.HTTPResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GETController extends HTTPHandler {
    public GETController(HTTPHandler next) {
        super(next);
    }

    @Override
    protected boolean matchHTTPMethod(HTTPMethod httpMethod) {
        return HTTPMethod.GET.equals(httpMethod);
    }

    @Override
    protected boolean matchHTTPPath(String httpPath) {
        return "/api/users".equals(httpPath);
    }

    @Override
    protected HTTPResponse process(HTTPRequest httpRequest) {
        HTTPResponse httpResponse = new HTTPResponse(200);
        Map<String, String> headers = new HashMap<>();
        headers.put("content-type", "application/json");
        headers.put("content-encoding", "UTF-8");
        httpResponse.setHttpHeaders(headers);

        List<Map<String, Object>> body = new ArrayList<>();
        Map<String, Object> item = new HashMap<>();
        item.put("id", 1);
        item.put("email", "abc@gmail.com");
        item.put("name", "abc");
        body.add(item);
        httpResponse.setResponseBody(body);
        return httpResponse;
    }
}

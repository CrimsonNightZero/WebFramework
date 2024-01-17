package org.web.domain.ext;

import org.web.domain.core.HTTPHandler;
import org.web.domain.core.HTTPMethod;
import org.web.domain.core.HTTPRequest;
import org.web.domain.core.HTTPResponse;

import java.util.HashMap;
import java.util.Map;

public class POSTController extends HTTPHandler {
    public POSTController(HTTPHandler next) {
        super(next);
    }

    @Override
    protected boolean matchHTTPMethod(HTTPMethod httpMethod) {
        return HTTPMethod.POST.equals(httpMethod);
    }

    @Override
    protected boolean matchHTTPPath(String httpPath) {
        return "/api/users".equals(httpPath);
    }

    @Override
    protected HTTPResponse process(HTTPRequest httpRequest) {
        try {
            validEmail(httpRequest.getRequestBody().get("email"));
            return new HTTPResponse(201);
        }catch (IllegalArgumentException ex){
            HTTPResponse httpResponse = new HTTPResponse(400);
            Map<String, String> headers = new HashMap<>();
            headers.put("content-type", "plain/text");
            headers.put("content-encoding", "UTF-8");
            httpResponse.setHttpHeaders(headers);

            httpResponse.setResponseBody("Registration's format incorrect.");
            return httpResponse;
        }
    }

    private void validEmail(String email){
        if (!email.contains("@")){
            throw new IllegalArgumentException("Illegal Email");
        }
    }
}

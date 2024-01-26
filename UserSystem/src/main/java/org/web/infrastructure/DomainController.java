package org.web.infrastructure;


import org.web.application.*;
import org.web.domain.User;
import org.web.domain.core.HTTPRequest;
import org.web.domain.core.HTTPResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DomainController {
    public DomainService domainService;

    public DomainController(DomainService domainService) {
        this.domainService = domainService;
    }

    public void setDomainService(DomainService domainService) {
        this.domainService = domainService;
    }

    public DomainService getDomainService() {
        return domainService;
    }

    public HTTPResponse registerUser(HTTPRequest httpRequest) {
        HTTPPOSTRequest httpPOSTRequest = httpRequest.readBodyAsObject(HTTPPOSTRequest.class);

        User user = domainService.registerUser(httpPOSTRequest);

        HTTPResponse httpResponse = new HTTPResponse(201);
        Map<String, String> headers = new HashMap<>();
        headers.put("content-type", "application/json");
        headers.put("content-encoding", "UTF-8");
        httpResponse.setHttpHeaders(headers);
        httpResponse.setBody(new HTTPPOSTResponse(user.getId(), user.getEmail(), user.getName()));
        return httpResponse;
    }

    public HTTPResponse login(HTTPRequest httpRequest) {
        HTTPLoginRequest httpLoginRequest = httpRequest.readBodyAsObject(HTTPLoginRequest.class);

        User user = domainService.login(httpLoginRequest);

        HTTPResponse httpResponse = new HTTPResponse(200);
        Map<String, String> headers = new HashMap<>();
        headers.put("content-type", "application/json");
        headers.put("content-encoding", "UTF-8");
        httpResponse.setHttpHeaders(headers);
        httpResponse.setBody(new HTTPLoginResponse(user.getId(), user.getEmail(), user.getName()));
        return httpResponse;
    }


    public HTTPResponse patch(HTTPRequest httpRequest) {
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
        httpResponse.setBody(body);
        return httpResponse;
    }

    public HTTPResponse get(HTTPRequest httpRequest) {
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
        httpResponse.setBody(body);
        return httpResponse;
    }
}

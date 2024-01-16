package org.web.domain;

import java.net.http.HttpHeaders;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HTTPServer {

    public HTTPResponse response(HTTPRequest httpRequest){
        if (httpRequest.getHttpPath().equals("/api/users")){
            if (httpRequest.getHttpMethod().equals(HTTPMethod.POST)){
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
            else if (httpRequest.getHttpMethod().equals(HTTPMethod.GET)){
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
        else if (httpRequest.getHttpPath().equals("/api/users/1")){
            if (httpRequest.getHttpMethod().equals(HTTPMethod.PATCH)){
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

        return null;
    }

    private void validEmail(String email){
        if (!email.contains("@")){
            throw new IllegalArgumentException("Illegal Email");
        }
    }
}

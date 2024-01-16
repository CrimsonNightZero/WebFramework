package org.web.domain;

import java.util.Map;

public class HTTPRequest {
    private HTTPMethod httpMethod;
    private String httpPath;
    private String httpQueryString;
    private Map<String, String> requestBody;
    private Map<String, String> httpHeaders;
    private HTTPServer httpServer;

    public HTTPMethod getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(HTTPMethod httpMethod) {
        this.httpMethod = httpMethod;
    }

    public String getHttpPath() {
        return httpPath;
    }

    public void setHttpPath(String httpPath) {
        this.httpPath = httpPath;
    }

    public String getHttpQueryString() {
        return httpQueryString;
    }

    public void setHttpQueryString(String httpQueryString) {
        this.httpQueryString = httpQueryString;
    }

    public Map<String, String> getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(Map<String, String> requestBody) {
        this.requestBody = requestBody;
    }

    public Map<String, String> getHttpHeaders() {
        return httpHeaders;
    }

    public void setHttpHeaders(Map<String, String> httpHeaders) {
        this.httpHeaders = httpHeaders;
    }

    public void setHttpServer(HTTPServer httpServer) {
        this.httpServer = httpServer;
    }

    public HTTPResponse send() {
        return httpServer.response(this);
    }
}

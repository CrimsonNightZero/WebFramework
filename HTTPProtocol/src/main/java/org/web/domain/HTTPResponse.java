package org.web.domain;

import java.util.Map;

public class HTTPResponse {
    private final int httpStatusCode;
    private Map<String, String> httpHeaders;
    private Object responseBody;
    public HTTPResponse(int httpStatusCode) {
        this.httpStatusCode = httpStatusCode;
    }

    public int getHttpStatusCode() {
        return httpStatusCode;
    }

    public Map<String, String> getHttpHeaders() {
        return httpHeaders;
    }

    public void setHttpHeaders(Map<String, String> httpHeaders) {
        this.httpHeaders = httpHeaders;
    }

    public <T> T getResponseBody() {
        return (T) responseBody;
    }

    public <T> void setResponseBody(T responseBody) {
        this.responseBody = responseBody;
    }
}

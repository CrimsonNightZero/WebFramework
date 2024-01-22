package org.web.domain.core;

public class HTTPResponse extends HTTPProtocol{
    private final int httpStatusCode;
    public HTTPResponse(int httpStatusCode) {
        this.httpStatusCode = httpStatusCode;
    }

    public int getHttpStatusCode() {
        return httpStatusCode;
    }

    public String getResponseBody() {
        return deserialization();
    }
}

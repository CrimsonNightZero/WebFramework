package org.web.domain.core;

public class HTTPResponse extends HTTPProtocol {
    private String httpVersion;
    private final int httpStatusCode;

    public HTTPResponse(int httpStatusCode) {
        this.httpVersion = "HTTP/1.1";
        this.httpStatusCode = httpStatusCode;
    }

    public void setHttpVersion(String httpVersion) {
        this.httpVersion = httpVersion;
    }

    public int getHttpStatusCode() {
        return httpStatusCode;
    }

    public String getResponseBody() {
        return String.format("""
                %s %d
                %s

                %s""", httpVersion, httpStatusCode, getHttpHeaderString(), getBody());
    }
}

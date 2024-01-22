package org.web.domain.core;

import org.web.domain.ext.DeserializationBodyTypeToJsonHandler;
import org.web.domain.ext.DeserializationBodyTypeToTextHandler;
import org.web.domain.ext.DeserializationBodyTypeToXMLHandler;

import java.util.Map;

public class HTTPResponse {
    private final int httpStatusCode;
    private Map<String, String> httpHeaders;
    private Object responseBody;
    private final DeserializationBodyTypeHandler deserializationBodyTypeHandler;
    public HTTPResponse(int httpStatusCode) {
        this.httpStatusCode = httpStatusCode;
        this.deserializationBodyTypeHandler = new DeserializationBodyTypeToJsonHandler(new DeserializationBodyTypeToXMLHandler(new DeserializationBodyTypeToTextHandler(null)));
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

    public String getResponseBody() {
        return deserializationBodyTypeHandler.handle(httpHeaders.get("content-type"), responseBody);
    }

    public <T> void setResponseBody(T responseBody) {
        this.responseBody = responseBody;
    }
}

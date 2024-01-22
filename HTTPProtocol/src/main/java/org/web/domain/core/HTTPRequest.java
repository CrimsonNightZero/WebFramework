package org.web.domain.core;

import org.web.domain.ext.SerializationBodyTypeToJsonHandler;
import org.web.domain.ext.SerializationBodyTypeToXMLHandler;

import java.util.Map;

public class HTTPRequest {
    private HTTPMethod httpMethod;
    private String httpPath;
    private String httpQueryString;
    private Object requestBody;
    private Map<String, String> httpHeaders;
    private final SerializationBodyTypeHandler serializationBodyTypeHandler;
    public HTTPRequest() {
        serializationBodyTypeHandler = new SerializationBodyTypeToJsonHandler(new SerializationBodyTypeToXMLHandler(null)) ;
    }

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

    public Object getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(Object requestBody) {
        this.requestBody = requestBody;
    }

    public Map<String, String> getHttpHeaders() {
        return httpHeaders;
    }

    public void setHttpHeaders(Map<String, String> httpHeaders) {
        this.httpHeaders = httpHeaders;
    }

    public <T> T readBodyAsObject(Class<?> httpRequestClass){
        return serializationBodyTypeHandler.handle(httpHeaders.get("content-type"), requestBody, httpRequestClass);
    }
}

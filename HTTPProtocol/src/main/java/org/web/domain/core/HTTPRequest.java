package org.web.domain.core;

public class HTTPRequest extends HTTPProtocol{
    private HTTPMethod httpMethod;
    private String httpPath;
    private String httpQueryString;

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

    public <T> T readBodyAsObject(Class<?> httpRequestClass){
        return serialization(httpRequestClass);
    }
}

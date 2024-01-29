package org.web.domain.core;

import java.util.HashMap;
import java.util.Map;

public class HTTPRequest extends HTTPProtocol{
    private HTTPMethod httpMethod;
    private HTTPPath httpPath;
    private String httpQueryString;

    public HTTPMethod getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(HTTPMethod httpMethod) {
        this.httpMethod = httpMethod;
    }

    public HTTPPath getHttpPath() {
        return httpPath;
    }

    public void setHttpPath(String path) {
        this.httpPath = new HTTPPath(path);
    }

    public void setHttpPath(HTTPPath httpPath) {
        this.httpPath = httpPath;
    }

    public String getHttpQueryString() {
        return httpQueryString;
    }

    public void setHttpQueryString(String httpQueryString) {
        this.httpQueryString = httpQueryString;
    }

    public <T> Map<String, T> getHttpQueryVariable() {
        return parseQueryString();
    }

    private <T> Map<String, T> parseQueryString(){
        Map<String, T> queryVariable = new HashMap<>();
        String[] queryStrings = httpQueryString.split("&");
        for (String queryString: queryStrings){
            String[] item = queryString.split("=");
            queryVariable.put(item[0], (T) item[1]);
        }
        return queryVariable;
    }

    public <T> T readBodyAsObject(Class<?> httpRequestClass){
        return serialization(httpRequestClass);
    }
}

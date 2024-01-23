package org.web.domain.core;

import org.web.domain.ext.protocol.*;

import java.util.Map;

public abstract class HTTPProtocol {
    protected Map<String, String> httpHeaders;

    protected Object body;
    private TransformBodyTypeHandler transformBodyTypeHandler;

    public HTTPProtocol() {
        this.transformBodyTypeHandler = null;
    }

    public Map<String, String> getHttpHeaders() {
        return httpHeaders;
    }

    public void setHttpHeaders(Map<String, String> httpHeaders) {
        this.httpHeaders = httpHeaders;
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }

    public void setTransformBodyTypeHandler(TransformBodyTypeHandler transformBodyTypeHandler){
        this.transformBodyTypeHandler = transformBodyTypeHandler;
    }

    public <T> T serialization(Class<?> transformClass){
        return transformBodyTypeHandler.serialize(httpHeaders.get("content-type"), body, transformClass);
    }

    public String deserialization() {
        return transformBodyTypeHandler.deserialize(httpHeaders.get("content-type"), body);
    }
}

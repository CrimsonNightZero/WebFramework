package org.web.domain.core;

import org.web.domain.ext.protocol.*;

import java.util.Map;

public abstract class HTTPProtocol {
    protected Map<String, String> httpHeaders;
    protected Object body;
    private final SerializationBodyTypeHandler serializationBodyTypeHandler;
    private final DeserializationBodyTypeHandler deserializationBodyTypeHandler;

    public HTTPProtocol() {
        this.serializationBodyTypeHandler = new SerializationBodyTypeToJsonHandler(new SerializationBodyTypeToXMLHandler(null));
        this.deserializationBodyTypeHandler = new DeserializationBodyTypeToJsonHandler(new DeserializationBodyTypeToXMLHandler(new DeserializationBodyTypeToTextHandler(null)));
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

    public <T> T serialization(Class<?> httpRequestClass){
        return serializationBodyTypeHandler.handle(httpHeaders.get("content-type"), body, httpRequestClass);
    }

    public String deserialization() {
        return deserializationBodyTypeHandler.handle(httpHeaders.get("content-type"), body);
    }
}

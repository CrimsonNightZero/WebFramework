package org.web.domain.core;

public abstract class SerializationBodyTypeHandler {
    private final SerializationBodyTypeHandler next;

    public SerializationBodyTypeHandler(SerializationBodyTypeHandler next) {
        this.next = next;
    }

    public <T> T handle(String contentType, Object requestBody, Class<?> httpRequestClass){
        if (matchContentType(contentType)){
            return transform(requestBody, httpRequestClass);
        }
        else {
            return next.handle(contentType, requestBody, httpRequestClass);
        }
    }

    protected abstract boolean matchContentType(String contentType);

    protected abstract <T> T transform(Object requestBody, Class<?> httpRequestClass);
}

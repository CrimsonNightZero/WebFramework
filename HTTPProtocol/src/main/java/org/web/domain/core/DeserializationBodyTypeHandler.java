package org.web.domain.core;

public abstract class DeserializationBodyTypeHandler {
    private final DeserializationBodyTypeHandler next;

    public DeserializationBodyTypeHandler(DeserializationBodyTypeHandler next) {
        this.next = next;
    }

    public String handle(String contentType, Object responseBody){
        return matchContentType(contentType)? transform(responseBody): next.handle(contentType, responseBody);
    }

    protected abstract boolean matchContentType(String contentType);

    protected abstract String transform(Object body);
}

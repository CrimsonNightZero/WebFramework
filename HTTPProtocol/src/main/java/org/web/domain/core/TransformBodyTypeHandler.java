package org.web.domain.core;

public abstract class TransformBodyTypeHandler {
    private final TransformBodyTypeHandler next;

    public TransformBodyTypeHandler(TransformBodyTypeHandler next) {
        this.next = next;
    }

    public <T> T serialize(String contentType, Object body, Class<?> transformClass){
        return matchContentType(contentType)? toObject(body, transformClass): next.serialize(contentType, body, transformClass);
    }

    protected abstract boolean matchContentType(String contentType);

    protected abstract <T> T toObject(Object body, Class<?> transformClass);

    public String deserialize(String contentType, Object body){
        return matchContentType(contentType)? toContent(body): next.deserialize(contentType, body);
    }

    protected abstract String toContent(Object body);
}

package org.web.domain.core;

public abstract class TransformBodyTypeHandler {
    private TransformBodyTypeHandler next;

    public TransformBodyTypeHandler() {
        this.next = null;
    }

    public void setNext(TransformBodyTypeHandler next) {
        this.next = next;
    }

    public <T> T serialize(String contentType, Object body, Class<?> transformClass) {
        if (matchContentType(contentType)){
            return toObject(body, transformClass);
        } else if (next != null) {
            return next.serialize(contentType, body, transformClass);
        }
        return null;
    }

    protected abstract boolean matchContentType(String contentType);

    protected abstract <T> T toObject(Object body, Class<?> transformClass);

    public String deserialize(String contentType, Object body) {
        if (matchContentType(contentType)){
            return toContent(body);
        } else if (next != null) {
            return next.deserialize(contentType, body);
        }
        return "";
    }

    protected abstract String toContent(Object body);
}

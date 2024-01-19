package org.web.domain.core;

import java.util.Map;

public abstract class TransformBodyTypeHandler {
    private final TransformBodyTypeHandler next;

    public TransformBodyTypeHandler(TransformBodyTypeHandler next) {
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

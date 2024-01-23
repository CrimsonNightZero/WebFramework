package org.web.domain.ext.protocol;

import org.web.domain.core.TransformBodyTypeHandler;

public class TransformBodyTypeToTextHandler extends TransformBodyTypeHandler {
    public TransformBodyTypeToTextHandler(TransformBodyTypeHandler next) {
        super(next);
    }

    @Override
    protected boolean matchContentType(String contentType) {
        return "plain/text".equals(contentType);
    }

    @Override
    protected <T> T toObject(Object body, Class<?> transformClass) {
        return null;
    }

    @Override
    protected String toContent(Object body) {
        return (String) body;
    }
}

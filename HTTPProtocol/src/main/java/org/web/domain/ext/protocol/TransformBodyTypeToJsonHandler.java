package org.web.domain.ext.protocol;

import org.web.domain.core.TransformBodyTypeHandler;
import org.web.infrastructure.FileUtil;

public class TransformBodyTypeToJsonHandler extends TransformBodyTypeHandler {
    public TransformBodyTypeToJsonHandler(TransformBodyTypeHandler next) {
        super(next);
    }

    @Override
    protected boolean matchContentType(String contentType) {
        return "application/json".equals(contentType);
    }

    @Override
    protected <T> T toObject(Object body, Class<?> transformClass) {
        return (T) FileUtil.readJsonValue(body, transformClass);
    }

    @Override
    protected String toContent(Object body) {
        return FileUtil.writeJsonValue(body);
    }
}

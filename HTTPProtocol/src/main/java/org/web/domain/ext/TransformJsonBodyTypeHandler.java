package org.web.domain.ext;

import org.web.infrastructure.FileUtil;
import org.web.domain.core.TransformBodyTypeHandler;

public class TransformJsonBodyTypeHandler extends TransformBodyTypeHandler {

    public TransformJsonBodyTypeHandler(TransformBodyTypeHandler next) {
        super(next);
    }

    @Override
    protected boolean matchContentType(String contentType) {
        return "application/json".equals(contentType);
    }

    @Override
    public <T> T transform(Object requestBody, Class<?> httpRequestClass) {
        return (T) FileUtil.readJsonValue(requestBody, httpRequestClass);
    }
}

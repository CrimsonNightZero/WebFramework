package org.web.domain.ext;

import org.web.infrastructure.FileUtil;
import org.web.domain.core.SerializationBodyTypeHandler;

public class SerializationBodyTypeToJsonHandler extends SerializationBodyTypeHandler {

    public SerializationBodyTypeToJsonHandler(SerializationBodyTypeHandler next) {
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

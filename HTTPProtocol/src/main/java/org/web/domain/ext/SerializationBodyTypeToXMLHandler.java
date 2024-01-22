package org.web.domain.ext;

import org.web.domain.core.SerializationBodyTypeHandler;
import org.web.infrastructure.FileUtil;

public class SerializationBodyTypeToXMLHandler extends SerializationBodyTypeHandler {
    public SerializationBodyTypeToXMLHandler(SerializationBodyTypeHandler next) {
        super(next);
    }

    @Override
    protected boolean matchContentType(String contentType) {
        return "application/xml".equals(contentType);
    }

    @Override
    protected <T> T transform(Object requestBody, Class<?> httpRequestClass) {
        return (T) FileUtil.readXMLValue(requestBody, httpRequestClass);
    }
}

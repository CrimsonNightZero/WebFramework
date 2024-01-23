package org.web.domain.ext.protocol;

import org.web.domain.core.TransformBodyTypeHandler;
import org.web.infrastructure.FileUtil;

public class TransformBodyTypeToXMLHandler extends TransformBodyTypeHandler {
    public TransformBodyTypeToXMLHandler(TransformBodyTypeHandler next) {
        super(next);
    }

    @Override
    protected boolean matchContentType(String contentType) {
        return "application/xml".equals(contentType);
    }

    @Override
    protected <T> T toObject(Object body, Class<?> transformClass) {
        return (T) FileUtil.readXMLValue(body, transformClass);
    }

    @Override
    protected String toContent(Object body) {
        return FileUtil.writeXMLValue(body, body.getClass());
    }
}

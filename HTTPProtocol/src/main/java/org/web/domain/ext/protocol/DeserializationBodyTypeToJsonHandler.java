package org.web.domain.ext.protocol;

import org.web.domain.core.DeserializationBodyTypeHandler;
import org.web.infrastructure.FileUtil;

public class DeserializationBodyTypeToJsonHandler extends DeserializationBodyTypeHandler {
    public DeserializationBodyTypeToJsonHandler(DeserializationBodyTypeHandler next) {
        super(next);
    }

    @Override
    protected boolean matchContentType(String contentType) {
        return "application/json".equals(contentType);
    }

    @Override
    protected String transform(Object body) {
        return FileUtil.writeJsonValue(body);
    }
}

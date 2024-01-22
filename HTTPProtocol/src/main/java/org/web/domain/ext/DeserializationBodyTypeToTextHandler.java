package org.web.domain.ext;

import org.web.domain.core.DeserializationBodyTypeHandler;

public class DeserializationBodyTypeToTextHandler extends DeserializationBodyTypeHandler {
    public DeserializationBodyTypeToTextHandler(DeserializationBodyTypeHandler next) {
        super(next);
    }

    @Override
    protected boolean matchContentType(String contentType) {
        return "plain/text".equals(contentType);
    }

    @Override
    protected String transform(Object responseBody) {
        return (String)responseBody;
    }
}

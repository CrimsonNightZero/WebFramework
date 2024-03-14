package org.web.domain.core;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public abstract class HTTPProtocol {
    protected Map<String, String> httpHeaders;

    protected Object body;

    public HTTPProtocol() {
    }

    public Map<String, String> getHttpHeaders() {
        return httpHeaders;
    }

    public void setHttpHeaders(Map<String, String> httpHeaders) {
        this.httpHeaders = httpHeaders;
    }

    public void setHttpHeaders(String httpHeaders) {
        this.httpHeaders = parserHttpHeaders(httpHeaders);
    }

    private Map<String, String> parserHttpHeaders(String httpHeaders) {
        HashMap<String, String> httpHeadersMap = new HashMap<>();
        for (String httpHeader : httpHeaders.split("\r\n")) {
            String[] header = httpHeader.split(": ");
            httpHeadersMap.put(header[0].toLowerCase(), header[1]);
        }
        return httpHeadersMap;
    }

    public String getHttpHeaderString() {
        if (Objects.isNull(httpHeaders)) {
            return "";
        }
        return httpHeaders.entrySet().stream()
                .map(httpHeader -> String.format("%s: %s\n", httpHeader.getKey().toLowerCase(), httpHeader.getValue()))
                .collect(Collectors.joining()).strip();
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }
}

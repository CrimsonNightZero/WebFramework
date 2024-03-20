package org.web.domain.core;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HTTPRequest extends HTTPProtocol {
    private HTTPMethod httpMethod;
    private HTTPPath httpPath;
    private String httpQueryString;
    private String httpVersion;

    public HTTPMethod getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(HTTPMethod httpMethod) {
        this.httpMethod = httpMethod;
    }

    public HTTPPath getHttpPath() {
        return httpPath;
    }

    public void setHttpPath(String path) {
        Matcher pathMatcher = parseHttpPath(path);
        if (!pathMatcher.find()) {
            return;
        }
        this.httpPath = new HTTPPath(parsePath(pathMatcher));
        setHttpMethod(HTTPMethod.valueOf(parseMethod(pathMatcher)));
        setHttpMethod(HTTPMethod.valueOf(parseMethod(pathMatcher)));
        setHttpQueryString(parseQueryString(pathMatcher));
        setHttpVersion(parsVersion(pathMatcher));
    }

    public void setHttpPath(HTTPPath httpPath) {
        this.httpPath = httpPath;
    }

    private Matcher parseHttpPath(String path) {
        String pattern = "^(?<method>\\S+)\s+(?<path>[^?]+)\\??(?<queryString>\\S+)?\s+(?<version>\\S+)$";
        return Pattern.compile(pattern).matcher(path);
    }

    private String parsePath(Matcher pathMatcher) {
        return pathMatcher.group("path");
    }

    private String parseMethod(Matcher pathMatcher) {
        return pathMatcher.group("method");
    }

    private String parseQueryString(Matcher pathMatcher) {
        return pathMatcher.group("queryString");
    }

    private String parsVersion(Matcher pathMatcher) {
        return pathMatcher.group("version");
    }

    public String getHttpQueryString() {
        return httpQueryString;
    }

    public void setHttpQueryString(String httpQueryString) {
        this.httpQueryString = httpQueryString;
    }

    public String getHttpVersion() {
        return httpVersion;
    }

    public void setHttpVersion(String httpVersion) {
        this.httpVersion = httpVersion;
    }

    public <T> Map<String, T> getHttpQueryVariable() {
        return parseQueryString();
    }

    private <T> Map<String, T> parseQueryString() {
        Map<String, T> queryVariable = new HashMap<>();
        if (Objects.isNull(httpQueryString) || httpQueryString.isBlank()) {
            return queryVariable;
        }
        String[] queryStrings = httpQueryString.split("&");
        for (String queryString : queryStrings) {
            String[] item = queryString.split("=");
            queryVariable.put(item[0], (T) item[1]);
        }
        return queryVariable;
    }
}

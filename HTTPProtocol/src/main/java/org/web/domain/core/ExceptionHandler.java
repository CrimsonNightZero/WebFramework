package org.web.domain.core;

import java.util.HashMap;
import java.util.Map;

public abstract class ExceptionHandler<T extends Exception> {
    private ExceptionHandler<?> next;
    private final Class<T> exception;
    private final int statusCode;

    public ExceptionHandler(Class<T> exception, int statusCode) {
        this.next = null;
        this.exception = exception;
        this.statusCode = statusCode;
    }

    public void setNext(ExceptionHandler<?> next) {
        this.next = next;
    }

    public HTTPResponse handle(HTTPRequest httpRequest, Throwable throwable) {
        return matchThrownException(throwable) ? response(httpRequest, throwable) : next.handle(httpRequest, throwable);
    }

    private boolean matchThrownException(Throwable throwable) {
        return throwable.getClass().equals(exception);
    }

    private HTTPResponse response(HTTPRequest httpRequest, Throwable throwable) {
        HTTPResponse httpResponse = new HTTPResponse(statusCode);
        Map<String, String> headers = new HashMap<>();
        headers.put("content-type", "plain/text");
        headers.put("content-encoding", "UTF-8");
        httpResponse.setHttpHeaders(headers);
        httpResponse.setBody(getExceptionMessage(httpRequest, throwable));
        return httpResponse;
    }

    protected String getExceptionMessage(HTTPRequest httpRequest, Throwable throwable) {
        return throwable.getMessage();
    }
}

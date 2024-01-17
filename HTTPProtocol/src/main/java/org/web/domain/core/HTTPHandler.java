package org.web.domain.core;

public abstract class HTTPHandler {

    private final HTTPHandler next;

    public HTTPHandler(HTTPHandler next) {
        this.next = next;
    }

    public HTTPResponse handle(HTTPRequest httpRequest){
        return match(httpRequest)? process(httpRequest): next.handle(httpRequest);
    }

    private boolean match(HTTPRequest httpRequest){
        return matchHTTPMethod(httpRequest.getHttpMethod()) && matchHTTPPath(httpRequest.getHttpPath());
    }

    protected abstract boolean matchHTTPMethod(HTTPMethod httpMethod);

    protected abstract boolean matchHTTPPath(String httpPath);

    protected abstract HTTPResponse process(HTTPRequest httpRequest);
}

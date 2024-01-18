package org.web.domain.core;

public interface HTTPHandler {
    HTTPResponse handle(HTTPRequest httpRequest);
}

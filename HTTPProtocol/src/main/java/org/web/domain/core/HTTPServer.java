package org.web.domain.core;

import org.web.domain.ext.GETController;
import org.web.domain.ext.PATCHController;
import org.web.domain.ext.POSTController;

public class HTTPServer {
    private final HTTPHandler httpHandler;

    public HTTPServer() {
        this.httpHandler = new POSTController(new PATCHController(new GETController(null)));
    }

    public HTTPResponse response(HTTPRequest httpRequest){
        return httpHandler.handle(httpRequest);
    }
}

package org.web.domain.ext;

import org.web.domain.core.HTTPRequest;
import org.web.domain.core.HTTPResponse;
import org.web.domain.core.HTTPServer;
import org.web.domain.core.ProtocolListener;

public class HTTPListener extends ProtocolListener {
    private final HTTPServer httpServer;

    public HTTPListener(int port, HTTPServer httpServer) {
        super(port);
        this.httpServer = httpServer;
    }

    @Override
    protected HTTPResponse handle(HTTPRequest httpRequest) {
       return httpServer.response(httpRequest);
    }
}

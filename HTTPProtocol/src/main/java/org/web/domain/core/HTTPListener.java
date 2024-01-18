package org.web.domain.core;

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

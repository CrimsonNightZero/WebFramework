package org.web.domain.core;

public abstract class ProtocolListener {
    private final int port;
    public ProtocolListener(int port) {
        this.port = port;
    }

    public HTTPResponse send(int port, HTTPRequest httpRequest){
        if (filterHTTPRequestByPort(port)){
            return null;
        }
        return handle(httpRequest);
    }

    private boolean filterHTTPRequestByPort(int port){
        return this.port != port;
    }

    protected abstract HTTPResponse handle(HTTPRequest httpRequest);
}

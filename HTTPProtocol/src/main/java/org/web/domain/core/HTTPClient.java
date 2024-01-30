package org.web.domain.core;

public class HTTPClient {
    private SocketAddress socketAddress;

    public void connect(SocketAddress socketAddress) {
        this.socketAddress = socketAddress;
    }

    public HTTPResponse send(HTTPRequest httpRequest) {
        return socketAddress.request(httpRequest);
    }

    public void close() {
        this.socketAddress = null;
    }
}

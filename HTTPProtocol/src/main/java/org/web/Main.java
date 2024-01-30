package org.web;

import org.web.domain.core.HTTPClient;
import org.web.domain.core.HTTPServer;
import org.web.domain.core.SocketAddress;

public class Main {
    public static void main(String[] args) {
        SocketAddress socketAddress = new SocketAddress(80);
        HTTPClient httpClient = new HTTPClient();
        httpClient.connect(socketAddress);

        HTTPServer httpServer = new HTTPServer();
        httpServer.listen(socketAddress);
    }
}

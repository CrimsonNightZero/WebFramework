package org.web;

import org.web.domain.core.HTTPClient;
import org.web.domain.core.SocketAddress;
import org.web.domain.core.WebApplication;

public class Main {
    public static void main(String[] args) {
        SocketAddress socketAddress = new SocketAddress(80);
        HTTPClient httpClient = new HTTPClient();
        httpClient.connect(socketAddress);

        WebApplication webApplication = new WebApplication();
        webApplication.listen(socketAddress);
    }
}

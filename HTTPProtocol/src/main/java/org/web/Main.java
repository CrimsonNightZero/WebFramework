package org.web;

import org.web.domain.core.HTTPServer;

public class Main {
    public static void main(String[] args) {
        HTTPServer httpServer = HTTPServer.create(8080);
        // httpServer.createContext(new DomainController());
        httpServer.start();
    }
}

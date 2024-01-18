package org.web;

import org.web.domain.core.*;

public class Main {
    public static void main(String[] args) {
        SocketAddress socketAddress = new SocketAddress(80);
        HTTPClient httpClient = new HTTPClient();
        httpClient.connect(socketAddress);

        HTTPServer httpServer = new HTTPServer();
        httpServer.listen(socketAddress);

        Router router = httpServer.getRouter();
        DomainController domainController = new DomainController();
        router.post("/api/users", domainController::post);
        router.patch("/api/users/1", domainController::patch);
        router.get("/api/users", domainController::get);
    }
}

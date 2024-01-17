package org.web;

import org.web.domain.core.DomainController;
import org.web.domain.core.HTTPServer;
import org.web.domain.core.Router;

public class Main {
    public static void main(String[] args) {
        HTTPServer httpServer = new HTTPServer();
        Router router = httpServer.getRouter();
        DomainController domainController = new DomainController();
        router.post("/api/users", domainController::post);
        router.patch("/api/users/1", domainController::patch);
        router.get("/api/users", domainController::get);
    }
}

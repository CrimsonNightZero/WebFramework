package org.web.domain.core;

public class HTTPServer {
    private final Router router;

    public HTTPServer() {
        this.router = new Router();
    }

    public Router getRouter() {
        return router;
    }

    public HTTPResponse response(HTTPRequest httpRequest){
        return router.execute(httpRequest);
    }
}

package org.web.domain.core;

public class WebApplication extends HTTPServer {
    private final Router router;

    public WebApplication() {
        this.router = new Router();
    }

    public Router getRouter() {
        return router;
    }

    public HTTPResponse response(HTTPRequest httpRequest){
        return router.execute(httpRequest);
    }
}

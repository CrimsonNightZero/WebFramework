package org.web.domain.core;

import org.web.domain.ext.exceptionshandler.NotAllowedMethodExceptionHandler;
import org.web.domain.ext.exceptionshandler.NotFindPathExceptionHandler;

public class WebApplication extends HTTPServer {
    private final Router router;

    public WebApplication() {
        this.router = new Router();
        registerBaseException();
    }

    private void registerBaseException() {
        registerException(new NotFindPathExceptionHandler());
        registerException(new NotAllowedMethodExceptionHandler());
    }

    public Router getRouter() {
        return router;
    }

    public HTTPResponse response(HTTPRequest httpRequest){
        try {
            return router.execute(httpRequest);
        }
        catch (Throwable ex){
            System.out.println(ex);
            return exceptionHandler.handle(httpRequest, ex);
        }
    }
}

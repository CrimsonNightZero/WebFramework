package org.web.domain.core;

import org.web.domain.ext.exceptions.NotAllowedMethodExceptionHandler;
import org.web.domain.ext.exceptions.NotExpectedExecutionHandler;
import org.web.domain.ext.exceptions.NotFindPathExceptionHandler;

public class WebApplication {
    private final HTTPServer httpServer;
    private final Container container;
    private final Router router;

    public WebApplication(int port) {
        this.httpServer = HTTPServer.create(port);
        this.container = new Container();
        this.router = new Router();
        router.setWebApplication(this);
        httpServer.createContext(router);
        registerBaseException();
    }

    public void launch(){
        httpServer.start();
    }

    private void registerBaseException() {
        router.registerException(new NotExpectedExecutionHandler());
        router.registerException(new NotFindPathExceptionHandler());
        router.registerException(new NotAllowedMethodExceptionHandler());
    }

    public Router getRouter() {
        return router;
    }

    public Container getContainer() {
        return container;
    }

    public void addException(ExceptionHandler<?> exceptionHandler){
        router.registerException(exceptionHandler);
    }

    public void addDataTypePlugin(TransformBodyTypeHandler transformBodyTypeHandler) {
        router.registerTransferDataType(transformBodyTypeHandler);
    }

    public void close(){
        httpServer.close();
    }
}

package org.web.domain.core;

import org.web.domain.ext.exceptions.NotAllowedMethodExceptionHandler;
import org.web.domain.ext.exceptions.NotFindPathExceptionHandler;

public class WebApplication extends HTTPServer {
    private final Container container;
    private final Router router;

    public WebApplication() {
        this.container = new Container();
        this.router = new Router();
        router.setWebApplication(this);
        registerBaseException();
    }

    private void registerBaseException() {
        registerException(new NotFindPathExceptionHandler());
        registerException(new NotAllowedMethodExceptionHandler());
    }

    public Router getRouter() {
        return router;
    }

    public Container getContainer() {
        return container;
    }

    public HTTPResponse response(HTTPRequest httpRequest){
        try {
            httpRequest.setTransformBodyTypeHandler(transformBodyTypeHandler);
            HTTPResponse response = router.execute(httpRequest);
            response.setTransformBodyTypeHandler(transformBodyTypeHandler);
            return response;
        }
        catch (Throwable ex){
            System.out.println(ex);
            ex.printStackTrace();
            httpRequest.setTransformBodyTypeHandler(transformBodyTypeHandler);
            HTTPResponse response = exceptionHandler.handle(httpRequest, ex);
            response.setTransformBodyTypeHandler(transformBodyTypeHandler);
            return response;
        }
    }

    public void addException(ExceptionHandler exceptionHandler){
        registerException(exceptionHandler);
    }

    public void addDataTypePlugin(TransformBodyTypeHandler transformBodyTypeHandler) {
        registerTransferDataType(transformBodyTypeHandler);
    }
}

package org.web.domain.core;

import org.web.domain.ext.exceptions.NotAllowedMethodExceptionHandler;
import org.web.domain.ext.exceptions.NotFindPathExceptionHandler;

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
            httpRequest.setTransformBodyTypeHandler(transformBodyTypeHandler);
            HTTPResponse response = router.execute(httpRequest);
            response.setTransformBodyTypeHandler(transformBodyTypeHandler);
            return response;
        }
        catch (Throwable ex){
            System.out.println(ex);
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

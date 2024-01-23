package org.web.domain.core;

import org.web.domain.ext.HTTPListener;
import org.web.domain.ext.exceptions.NotExpectedExecutionHandler;

public class HTTPServer {
    private HTTPListener httpPortListener;
    private SocketAddress socketAddress;
    private HTTPHandler httpHandler;
    protected ExceptionHandler exceptionHandler;
    protected TransformBodyTypeHandler transformBodyTypeHandler;

    public HTTPServer() {
        this.exceptionHandler = new NotExpectedExecutionHandler();
    }

    public void createContext(HTTPHandler httpHandler){
        this.httpHandler = httpHandler;
    }

    public void listen(SocketAddress socketAddress){
        this.httpPortListener = new HTTPListener(socketAddress.getPort(),this);
        socketAddress.subscribe(httpPortListener);
        this.socketAddress = socketAddress;
    }

    public void registerException(ExceptionHandler exceptionHandler){
        exceptionHandler.setNext(this.exceptionHandler);
        this.exceptionHandler = exceptionHandler;
    }

    public void registerTransferDataType(TransformBodyTypeHandler transformBodyTypeHandler) {
        transformBodyTypeHandler.setNext(this.transformBodyTypeHandler);
        this.transformBodyTypeHandler = transformBodyTypeHandler;
    }

    public HTTPResponse response(HTTPRequest httpRequest){
        try {
            httpRequest.setTransformBodyTypeHandler(transformBodyTypeHandler);
            HTTPResponse response = httpHandler.handle(httpRequest);
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

    public void close(){
        socketAddress.unsubscribe(httpPortListener);
        this.httpPortListener = null;
    }
}

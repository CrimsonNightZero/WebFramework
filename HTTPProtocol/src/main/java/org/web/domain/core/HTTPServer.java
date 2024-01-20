package org.web.domain.core;


import org.web.domain.ext.exceptionshandler.NotExpectedExecutionHandler;

public class HTTPServer {
    private HTTPListener httpPortListener;
    private SocketAddress socketAddress;
    private HTTPHandler httpHandler;
    protected ExceptionHandler exceptionHandler;

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

    public HTTPResponse response(HTTPRequest httpRequest){
        try {
            return httpHandler.handle(httpRequest);
        }
        catch (Throwable ex){
            System.out.println(ex);
            return exceptionHandler.handle(httpRequest, ex);
        }
    }

    public void close(){
        socketAddress.unsubscribe(httpPortListener);
        this.httpPortListener = null;
    }
}

package org.web.domain.core;


public class HTTPServer {
    private HTTPListener httpPortListener;
    private SocketAddress socketAddress;
    private HTTPHandler httpHandler;

    public HTTPServer() {}

    public void createContext(HTTPHandler httpHandler){
        this.httpHandler = httpHandler;
    }

    public void listen(SocketAddress socketAddress){
        this.httpPortListener = new HTTPListener(socketAddress.getPort(),this);
        socketAddress.subscribe(httpPortListener);
        this.socketAddress = socketAddress;
    }

    public HTTPResponse response(HTTPRequest httpRequest){
        return httpHandler.handle(httpRequest);
    }

    public void close(){
        socketAddress.unsubscribe(httpPortListener);
        this.httpPortListener = null;
    }
}

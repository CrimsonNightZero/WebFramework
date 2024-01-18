package org.web.domain.core;

public class HTTPServer {
    private final Router router;
    private HTTPListener httpPortListener;
    private SocketAddress socketAddress;

    public HTTPServer() {
        this.router = new Router();
    }

    public Router getRouter() {
        return router;
    }

    public void listen(SocketAddress socketAddress){
        this.httpPortListener = new HTTPListener(socketAddress.getPort(),this);
        socketAddress.subscribe(httpPortListener);
        this.socketAddress = socketAddress;
    }

    public void close(){
        socketAddress.unsubscribe(httpPortListener);
        this.httpPortListener = null;
    }

    public HTTPResponse response(HTTPRequest httpRequest){
        return router.execute(httpRequest);
    }
}

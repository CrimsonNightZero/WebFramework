package org.web.domain.core;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class HTTPServer {
    private final int port;
    private ServerSocket serverSocket;
    private final List<HTTPHandler> httpHandlers;
    private Thread daemon;

    protected HTTPServer(int port) {
        this.port = port;
        this.httpHandlers = new ArrayList<>();
    }

    public static HTTPServer create(int port) {
        return new HTTPServer(port);
    }

    public void start() {
        this.daemon = new Thread(this::run);
        daemon.start();
    }

    private void run() {
        try (ServerSocket serverSocket = new ServerSocket()) {
            this.serverSocket = serverSocket;
            serverSocket.bind(new InetSocketAddress(port));
            System.out.println("Server started. Listening for connections on port " + port + "...");

            while (serverSocket.isBound()) {
                Socket clientSocket = serverSocket.accept();
                response(clientSocket);
            }
        } catch (IOException e) {
            close();
        }
    }

    public void createContext(HTTPHandler httpHandler) {
        this.httpHandlers.add(httpHandler);
    }

    public void response(Socket clientSocket) {
        httpHandlers.forEach(httpHandler -> {
            httpHandler.setClientSocket(clientSocket);
            httpHandler.run();
        });
    }

    public void close() {
        try {
            serverSocket.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        daemon.interrupt();
    }
}

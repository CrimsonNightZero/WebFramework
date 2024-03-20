package org.web.domain.core;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public abstract class HTTPHandler {
    protected Socket clientSocket;

    public void setClientSocket(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    public void run() {
        try{
            writeResponse(handle(readRequest(getBufferedReader())));
            close();
        } catch (Throwable e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    protected BufferedReader getBufferedReader() throws IOException {
        return new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    }

    protected HTTPRequest readRequest(BufferedReader reader) throws IOException {
        HTTPRequest httpRequest = new HTTPRequest();
        httpRequest.setHttpPath(getRequestPath(reader));
        httpRequest.setHttpHeaders(getRequestHeaders(reader));
        httpRequest.setBody(getRequestBody(reader, Integer.parseInt(httpRequest.getHttpHeaders().get("content-length"))));
        return httpRequest;
    }

    private static String getRequestBody(BufferedReader reader, int contentLength) throws IOException {
        StringBuilder requestBody = new StringBuilder();
        while (requestBody.length() != contentLength) {
            requestBody.append((char) reader.read());
        }
        return requestBody.toString();
    }

    private String getRequestPath(BufferedReader reader) throws IOException {
        String line = reader.readLine();
        return (line != null && !line.isEmpty()) ? line : "";
    }

    private String getRequestHeaders(BufferedReader reader) throws IOException {
        StringBuilder requestHeaders = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null && !line.isEmpty()) {
            requestHeaders.append(line).append("\r\n");
        }
        return requestHeaders.toString();
    }

    protected void writeResponse(HTTPResponse response) throws IOException {
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
        writer.write(response.getResponseBody());
        writer.flush();
    }

    protected void close() throws IOException {
        clientSocket.close();
    }

    protected abstract HTTPResponse handle(HTTPRequest httpRequest) throws Throwable;
}

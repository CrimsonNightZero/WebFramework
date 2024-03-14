package org.web.domain.core;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public abstract class HTTPHandler {
    private Socket clientSocket;

    public void setClientSocket(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    // @Override
    public void run() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
            writeResponse(handle(readRequest(reader)));
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    protected HTTPRequest readRequest(BufferedReader reader) throws IOException {
        HTTPRequest httpRequest = new HTTPRequest();
        httpRequest.setHttpPath(getRequestPath(reader));
        httpRequest.setHttpHeaders(getRequestHeaders(reader));
        httpRequest.setBody(getRequestBody(reader));
        return httpRequest;
    }

    private static String getRequestBody(BufferedReader reader) throws IOException {
        StringBuilder requestBody = new StringBuilder();
        while (reader.ready()) {
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

    protected abstract HTTPResponse handle(HTTPRequest httpRequest);
}

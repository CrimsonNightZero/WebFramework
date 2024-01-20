import mock.DomainController;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.web.domain.core.*;
import org.web.domain.ext.exceptionshandler.NotAllowedMethodExceptionHandler;
import org.web.domain.ext.exceptionshandler.NotExpectedExecutionHandler;
import org.web.domain.ext.exceptionshandler.NotFindPathExceptionHandler;

import java.util.HashMap;
import java.util.Map;

public class HandleExceptionTestCase {
    private HTTPServer httpServer;
    private HTTPClient httpClient;

    @BeforeEach
    void test(){
        SocketAddress socketAddress = new SocketAddress(80);
        this.httpClient = new HTTPClient();
        httpClient.connect(socketAddress);

        this.httpServer = new HTTPServer();
        httpServer.listen(socketAddress);
        httpServer.createContext(new DomainController());
        httpServer.registerException(new NotFindPathExceptionHandler());
        httpServer.registerException(new NotAllowedMethodExceptionHandler());
    }

    @AfterEach
    void close(){
        httpClient.close();
        httpServer.close();
    }
    /*
       Given
           HTTP
               headers:
                   content-type: application/json
               method: POST
               path: /api/notfound


       When
           Send a POST request

       Then
           HTTP
               status code: 404
               headers:
                   content-type: plain/text
                   content-encoding: UTF-8
               body: Cannot find the path "/api/notfound"
    */
    @Test
    void notFindPath(){
        // Given
        HTTPRequest httpRequest = new HTTPRequest();
        Map<String, String> headers = new HashMap<>();
        headers.put("content-type", "application/json");
        httpRequest.setHttpHeaders(headers);
        httpRequest.setHttpMethod(HTTPMethod.POST);
        httpRequest.setHttpPath("/api/notfound");

        // When
        HTTPResponse response = httpClient.send(httpRequest);

        // Then
        Assertions.assertEquals(404, response.getHttpStatusCode());
        Map<String, String> httpHeaders = response.getHttpHeaders();
        Assertions.assertEquals("plain/text", httpHeaders.get("content-type"));
        Assertions.assertEquals("UTF-8", httpHeaders.get("content-encoding"));
        Assertions.assertEquals("Cannot find the path /api/notfound", response.getResponseBody());
    }

    /*
       Given
           HTTP
               headers:
                   content-type: application/json
               method: DELETE
               path: /api/users


       When
           Send a DELETE request

       Then
           HTTP
               status code: 405
               headers:
                   content-type: plain/text
                   content-encoding: UTF-8
               body: The method "DELETE" is not allowed on "/api/users"
    */
    @Test
    void notAllowedMethod(){
        // Given
        HTTPRequest httpRequest = new HTTPRequest();
        Map<String, String> headers = new HashMap<>();
        headers.put("content-type", "application/json");
        httpRequest.setHttpHeaders(headers);
        httpRequest.setHttpMethod(HTTPMethod.DELETE);
        httpRequest.setHttpPath("/api/users");

        // When
        HTTPResponse response = httpClient.send(httpRequest);

        // Then
        Assertions.assertEquals(405, response.getHttpStatusCode());
        Map<String, String> httpHeaders = response.getHttpHeaders();
        Assertions.assertEquals("plain/text", httpHeaders.get("content-type"));
        Assertions.assertEquals("UTF-8", httpHeaders.get("content-encoding"));
        Assertions.assertEquals("The method DELETE is not allowed on /api/users", response.getResponseBody());
    }

    /*
       Given
           HTTP
               headers:
                   content-type: application/json

           User info:
           {
               "email": "abc@gmail.com",
               "name": 1,
               "password": 2
           }

        When
            Send a POST request

       Then
           HTTP
               status code: 500
               headers:
                   content-type: plain/text
                   content-encoding: UTF-8
               body: The exception is not expected
    */
    @Test
    void notExpectedExecution(){
        // Given
        HTTPRequest httpRequest = new HTTPRequest();
        Map<String, String> headers = new HashMap<>();
        headers.put("content-type", "application/json");
        httpRequest.setHttpHeaders(headers);
        httpRequest.setHttpMethod(HTTPMethod.POST);
        httpRequest.setHttpPath("/api/users");

        String email = "abc@gmail.com";
        int name = 1;
        int password = 2;
        String body = String.format("""
               {
                   "email": "%s",
                   "name": %d,
                   "password" %d
               }
                """, email, name, password);
        httpRequest.setRequestBody(body);

        // When
        HTTPResponse response = httpClient.send(httpRequest);

        // Then
        Assertions.assertEquals(500, response.getHttpStatusCode());
        Map<String, String> httpHeaders = response.getHttpHeaders();
        Assertions.assertEquals("plain/text", httpHeaders.get("content-type"));
        Assertions.assertEquals("UTF-8", httpHeaders.get("content-encoding"));
        Assertions.assertEquals("The exception is not expected", response.getResponseBody());
    }
}

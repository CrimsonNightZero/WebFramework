import mock.DomainController;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.web.domain.core.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HTTPProtocolTestCase {
    private HTTPClient httpClient;
    private HTTPServer httpServer;
    @BeforeEach
    void setUp(){
        SocketAddress socketAddress = new SocketAddress(80);
        this.httpClient = new HTTPClient();
        httpClient.connect(socketAddress);

        this.httpServer = new HTTPServer();
        httpServer.listen(socketAddress);
        httpServer.createContext(new DomainController());
    }
    @AfterEach
    void tearDown(){
        httpClient.close();
        httpServer.close();
    }
    /*
       Given
           HTTP
               headers:
                   content-type: application/json
               method: POST
               path: /api/users

           User info:
           {
               "email": "abc@gmail.com",
               "name": "abc",
               "password": "hello"
           }

       When
           Send a POST request

       Then
           HTTP
               status code: 201
    */
    @Test
    void sendPOSTRequest(){
        // Given
        HTTPRequest httpRequest = new HTTPRequest();

        httpRequest.setHttpMethod(HTTPMethod.POST);

        httpRequest.setHttpPath("/api/users");

        Map<String, String> headers = new HashMap<>();
        headers.put("content-type", "application/json");
        httpRequest.setHttpHeaders(headers);

        String email = "abc@gmail.com";
        String name = "abc";
        String password = "hello";
        String body = String.format("""
               {
                   "email": "%s",
                   "name": "%s",
                   "password": "%s"
               }
                """, email, name, password);
        httpRequest.setRequestBody(body);

        // When
        HTTPResponse response = httpClient.send(httpRequest);

        // Then
        Assertions.assertEquals(201, response.getHttpStatusCode());
    }

    /*
        Given
            HTTP
                headers:
                    content-type: application/json
                    Authorization: Bearer <token>
                method: PATCH
                path: /api/users/{userId=1}

            User info:
            {
                "newName": "newAbc"
            }

        When
            Send a PATCH request

        Then
            HTTP
                status code: 200
                headers:
                   content-type: application/json
                   content-encoding: UTF-8

           User info:
           {
               "id": 1,
               "email": "abc@gmail.com",
               "name": "newAbc",
               "password": "hello"
           }
     */
    @Test
    void sendPATCHRequest(){
        // Given
        HTTPRequest httpRequest = new HTTPRequest();

        httpRequest.setHttpMethod(HTTPMethod.PATCH);

        httpRequest.setHttpPath("/api/users/1");

        Map<String, String> headers = new HashMap<>();
        headers.put("content-type", "application/json");
        headers.put("Authorization", "Bearer <token>");
        httpRequest.setHttpHeaders(headers);

        String newName = "newAbc";
        String body = String.format("""
               {
                   "newName": "%s"
               }
                """, newName);
        httpRequest.setRequestBody(body);

        // When
        HTTPResponse response = httpClient.send(httpRequest);

        // Then
        Assertions.assertEquals(200, response.getHttpStatusCode());
        Map<String, String> httpHeaders = response.getHttpHeaders();
        Assertions.assertEquals("application/json", httpHeaders.get("content-type"));
        Assertions.assertEquals("UTF-8", httpHeaders.get("content-encoding"));
        Map<String, Object> responseBody = response.getResponseBody();
        Assertions.assertEquals(1, responseBody.get("id"));
        Assertions.assertEquals("abc@gmail.com", responseBody.get("email"));
        Assertions.assertEquals("newAbc", responseBody.get("name"));
        Assertions.assertEquals("hello", responseBody.get("password"));
    }

    /*
        Given
            HTTP
                headers:
                    Authorization: Bearer <token>
                method: GET
                path: /api/users
                query: keyword=abc

        When
            Send a GET request

        Then
            HTTP
                status code: 200
                headers:
                    content-type: application/json
                    content-encoding: UTF-8

            User info:
            [
                {
                    "id": 1,
                    "email": "abc@gmail.com",
                    "name": "abc"
                }
            ]
     */
    @Test
    void sendGETRequest(){
        // Given
        HTTPRequest httpRequest = new HTTPRequest();

        httpRequest.setHttpMethod(HTTPMethod.GET);

        httpRequest.setHttpPath("/api/users");

        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer <token>");
        httpRequest.setHttpHeaders(headers);

        httpRequest.setHttpQueryString("keyword=abc");

        // When
        HTTPResponse response = httpClient.send(httpRequest);

        // Then
        Assertions.assertEquals(200, response.getHttpStatusCode());
        Map<String, String> httpHeaders = response.getHttpHeaders();
        Assertions.assertEquals("application/json", httpHeaders.get("content-type"));
        Assertions.assertEquals("UTF-8", httpHeaders.get("content-encoding"));
        List<Map<String, Object>> responseBody = response.getResponseBody();
        Map<String, Object> item = responseBody.get(0);
        Assertions.assertEquals(1, item.get("id"));
        Assertions.assertEquals("abc@gmail.com", item.get("email"));
        Assertions.assertEquals("abc", item.get("name"));
    }

    /*
        Given
            HTTP
                headers:
                    content-type: application/json
                method: POST
                path: /api/users

            User info:
            {
                "email": "efg",
                "name": "efg",
                "password": "hello",
            }

        When
            Send a POST request

        Then
            HTTP
                status code: 400
                headers:
                    content-type: plain/text
                    content-encoding: UTF-8

            User info: Registration's format incorrect.
     */
    @Test
    void sendExceptionRequest(){
        // Given
        HTTPRequest httpRequest = new HTTPRequest();

        httpRequest.setHttpMethod(HTTPMethod.POST);

        httpRequest.setHttpPath("/api/users");

        Map<String, String> headers = new HashMap<>();
        headers.put("content-type", "application/json");
        httpRequest.setHttpHeaders(headers);

        String email = "efg";
        String name = "efg";
        String password = "hello";
        String body = String.format("""
               {
                   "email": "%s",
                   "name": "%s",
                   "password": "%s"
               }
                """, email, name, password);
        httpRequest.setRequestBody(body);

        // When
        HTTPResponse response = httpServer.response(httpRequest);

        // Then
        Assertions.assertEquals(400, response.getHttpStatusCode());
        Map<String, String> httpHeaders = response.getHttpHeaders();
        Assertions.assertEquals("plain/text", httpHeaders.get("content-type"));
        Assertions.assertEquals("UTF-8", httpHeaders.get("content-encoding"));
        Assertions.assertEquals("Registration's format incorrect.", response.getResponseBody());
    }
}

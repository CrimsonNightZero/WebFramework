import mock.DomainController;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.web.domain.core.*;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;

public class HTTPProtocolTestCase {
    private HTTPServer httpServer;
    @BeforeEach
    void setUp(){
        this.httpServer = HTTPServer.create(8080);
        httpServer.createContext(new DomainController());
        httpServer.start();
    }
    @AfterEach
    void tearDown(){
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
    void sendPOSTRequest() throws IOException, InterruptedException {
        // Given
        HttpClient httpClient = HttpClient.newBuilder().build();

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

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://127.0.0.1:8080/api/users?a=1"))
                .setHeader("content-type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

        // When
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        // Then
        Assertions.assertEquals(201, response.statusCode());
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
    void sendPATCHRequest() throws IOException, InterruptedException {
        // Given
        HttpClient httpClient = HttpClient.newBuilder().build();
        String newName = "newAbc";
        String body = String.format("""
               {
                   "newName": "%s"
               }
                """, newName);

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://127.0.0.1:8080/api/users/1"))
                .setHeader("content-type", "application/json")
                .setHeader("Authorization", "Bearer <token>")
                .method("PATCH", HttpRequest.BodyPublishers.ofString(body)).build();

        // When
        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        // Then
        Assertions.assertEquals(200, response.statusCode());
        Map<String, List<String>> httpHeaders = response.headers().map();
        Assertions.assertEquals("application/json", httpHeaders.get("content-type").get(0));
        Assertions.assertEquals("UTF-8", httpHeaders.get("content-encoding").get(0));
        String responseBody = response.body();
        Assertions.assertEquals("""
                {password=hello, name=newAbc, id=1, email=abc@gmail.com}""", responseBody);
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
    void sendGETRequest() throws IOException, InterruptedException {
        // Given
        HttpClient httpClient = HttpClient.newBuilder().build();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://127.0.0.1:8080/api/users?keyword=abc"))
                .setHeader("Authorization", "Bearer <token>")
                .GET()
                .build();

        // When
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        // Then
        Assertions.assertEquals(200, response.statusCode());
        Map<String, List<String>> httpHeaders = response.headers().map();
        Assertions.assertEquals("application/json", httpHeaders.get("content-type").get(0));
        Assertions.assertEquals("UTF-8", httpHeaders.get("content-encoding").get(0));
        String responseBody = response.body();
        Assertions.assertEquals("""
               [{name=abc, id=1, email=abc@gmail.com}]""", responseBody);
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
    void sendExceptionRequest() throws IOException, InterruptedException {
        // Given
        HttpClient httpClient = HttpClient.newBuilder().build();
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

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://127.0.0.1:8080/api/users"))
                .setHeader("content-type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body)).build();

        // When
        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        // Then
        Assertions.assertEquals(400, response.statusCode());
        Map<String, List<String>> httpHeaders = response.headers().map();
        Assertions.assertEquals("plain/text", httpHeaders.get("content-type").get(0));
        Assertions.assertEquals("UTF-8", httpHeaders.get("content-encoding").get(0));
        Assertions.assertEquals("Registration's format incorrect.", response.body());
    }
}

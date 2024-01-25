import mock.DomainController;
import mock.DomainService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.web.domain.core.*;
import org.web.domain.ext.HTTPRequestScope;
import org.web.domain.ext.PrototypeScope;
import org.web.domain.ext.SingletonScope;
import org.web.domain.ext.protocol.TransformBodyTypeToJsonHandler;
import org.web.domain.ext.protocol.TransformBodyTypeToTextHandler;
import org.web.domain.ext.protocol.TransformBodyTypeToXMLHandler;

import java.util.HashMap;
import java.util.Map;

public class WebFrameworkTestCase {
    private HTTPClient httpClient;
    private WebApplication webApplication;

    @BeforeEach
    void setUp(){
        SocketAddress socketAddress = new SocketAddress(80);
        this.httpClient = new HTTPClient();
        httpClient.connect(socketAddress);

        this.webApplication = new WebApplication();
        webApplication.listen(socketAddress);

        Container container = webApplication.getContainer();
        container.register(DomainController.class, new SingletonScope());
        container.register(DomainService.class, new PrototypeScope());

        Router router = webApplication.getRouter();
        router.post("/api/users", DomainController.class, "post");
        router.patch("/api/users/1", DomainController.class, "patch");
        router.get("/api/users", DomainController.class, "get");

        webApplication.addDataTypePlugin(new TransformBodyTypeToTextHandler());
        webApplication.addDataTypePlugin(new TransformBodyTypeToXMLHandler());
        webApplication.addDataTypePlugin(new TransformBodyTypeToJsonHandler());
    }
    @AfterEach
    void tearDown(){
        httpClient.close();
        webApplication.close();
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
        httpRequest.setBody(body);

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
        httpRequest.setBody(body);

        // When
        HTTPResponse response = httpClient.send(httpRequest);

        // Then
        Assertions.assertEquals(200, response.getHttpStatusCode());
        Map<String, String> httpHeaders = response.getHttpHeaders();
        Assertions.assertEquals("application/json", httpHeaders.get("content-type"));
        Assertions.assertEquals("UTF-8", httpHeaders.get("content-encoding"));
        String responseBody = response.getResponseBody();
        Assertions.assertEquals("""
                {"password":"hello","name":"newAbc","id":1,"email":"abc@gmail.com"}""", responseBody);
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
        String responseBody = response.getResponseBody();
        Assertions.assertEquals("""
               [{"name":"abc","id":1,"email":"abc@gmail.com"}]""", responseBody);
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
        httpRequest.setBody(body);

        // When
        HTTPResponse response = httpClient.send(httpRequest);

        // Then
        Assertions.assertEquals(400, response.getHttpStatusCode());
        Map<String, String> httpHeaders = response.getHttpHeaders();
        Assertions.assertEquals("plain/text", httpHeaders.get("content-type"));
        Assertions.assertEquals("UTF-8", httpHeaders.get("content-encoding"));
        Assertions.assertEquals("Registration's format incorrect.", response.getResponseBody());
    }
}

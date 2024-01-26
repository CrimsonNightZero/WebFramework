import org.web.infrastructure.DomainController;
import org.web.application.DomainService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.web.domain.core.*;
import org.web.domain.ext.SingletonScope;
import org.web.domain.ext.protocol.TransformBodyTypeToJsonHandler;
import org.web.domain.ext.protocol.TransformBodyTypeToTextHandler;
import org.web.domain.ext.protocol.TransformBodyTypeToXMLHandler;
import org.web.infrastructure.exceptions.DuplicateEmailHandler;
import org.web.infrastructure.exceptions.IncorrectFormatOfRegistrationHandler;

import java.util.HashMap;
import java.util.Map;

public class WebAPPTestCase {
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
        container.register(DomainService.class, new SingletonScope());

        Router router = webApplication.getRouter();
        router.post("/api/users", DomainController.class, "registerUser");
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
                "password": "hello",
            }

        When
            Register a user

        Then
            HTTP
                status code: 201
                headers:
                    content-type: application/json
                    content-encoding: UTF-8

            User info:
            {
                "id": 1,
                "email": "abc@gmail.com",
                "name": "abc"
            }
     */
    @Test
    void UserRegistration(){
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
        Assertions.assertEquals("application/json", response.getHttpHeaders().get("content-type"));
        Assertions.assertEquals("UTF-8", response.getHttpHeaders().get("content-encoding"));
        Assertions.assertEquals("""
                {"id":1,"email":"abc@gmail.com","name":"abc"}""", response.getResponseBody());
    }

    /*
        // {statusCode: 400, headers: {content-type: plain/text, content-encoding: UTF-8}, responseBody: Registration's format incorrect.}
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
                "password": "hello",
            }

        When
            Register a user

        Then
            HTTP
                status code: 400
                headers:
                {
                    content-type: plain/text,
                    content-encoding: UTF-8
                }
                responseBody: Duplicate email
     */
    @Test
    void UserRegistrationOnDuplicateEmailFail(){
        // Given
        webApplication.addException(new DuplicateEmailHandler());
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
        httpClient.send(httpRequest);
        HTTPResponse response = httpClient.send(httpRequest);

        // Then
        Assertions.assertEquals(400, response.getHttpStatusCode());
        Assertions.assertEquals("plain/text", response.getHttpHeaders().get("content-type"));
        Assertions.assertEquals("UTF-8", response.getHttpHeaders().get("content-encoding"));
        Assertions.assertEquals("Duplicate email", response.getResponseBody());
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
                "email": "abc",
                "name": "abc",
                "password": "hello",
            }

        When
            Register a user

        Then
            HTTP
                status code: 400
                headers:
                {
                    content-type: plain/text,
                    content-encoding: UTF-8
                }
                responseBody: Registration's format incorrect.
     */
    @Test
    void UserRegistrationOnIncorrectFormatOfRegistrationFail(){
        // Given
        webApplication.addException(new IncorrectFormatOfRegistrationHandler());
        HTTPRequest httpRequest = new HTTPRequest();

        httpRequest.setHttpMethod(HTTPMethod.POST);

        httpRequest.setHttpPath("/api/users");

        Map<String, String> headers = new HashMap<>();
        headers.put("content-type", "application/json");
        httpRequest.setHttpHeaders(headers);

        String email = "abc";
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
        Assertions.assertEquals(400, response.getHttpStatusCode());
        Assertions.assertEquals("plain/text", response.getHttpHeaders().get("content-type"));
        Assertions.assertEquals("UTF-8", response.getHttpHeaders().get("content-encoding"));
        Assertions.assertEquals("Registration's format incorrect.", response.getResponseBody());
    }

    /*
        Given
            HTTP
                headers:
                    content-type: application/json
                method: POST
                path: /api/users/login

            User info:
            {
                "email": "abc@gmail.com",
                "password": "hello",
            }

        When
            User login

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
                "name": "abc",
                "token": "67cbbe1b-0c10-46e4-b4a8-9e8505c2453b"
            }
     */
    @Test
    void UserLogin(){

    }

    /*
        // {statusCode: 400, headers: {content-type: plain/text, content-encoding: UTF-8}, responseBody: Credentials Invalid}
        // {statusCode: 400, headers: {content-type: plain/text, content-encoding: UTF-8}, responseBody: Login's format incorrect.}
        Given
            HTTP
                headers:
                    content-type: application/json
                method: POST
                path: /api/users

            User info:
            {
                "email": "abc@gmail.com",
                "password": "hello",
            }

        When
            User login

        Then
            HTTP
                status code: {statusCode}
                headers: {headers}

            User info: {responseBody}
     */
    @Test
    void userLoginOnFail(){

    }

    /*
        Given
            HTTP
                headers:
                    content-type: application/json
                    Authorization: Bearer <token>
                method: PATCH
                path: /api/users/1

            User info:
            {
                "newName": "newAbc"
            }

        When
            User rename

        Then
            HTTP
                status code: 204
     */
    @Test
    void UserRename(){

    }

    /* // {statusCode: 401, headers: {content-type: plain/text, content-encoding: UTF-8}, responseBody: Can't authenticate who you are.}
       // {statusCode: 403, headers: {content-type: plain/text, content-encoding: UTF-8}, responseBody: Forbidden}
       // {statusCode: 400, headers: {content-type: plain/text, content-encoding: UTF-8}, responseBody: Name's format invalid.}
        Given
            HTTP
                headers:
                    content-type: application/json
                    Authorization: Bearer <token>
                method: PATCH
                path: /api/users/0

            User info:
            {
                "newName": "newAbc"
            }

        When
            User rename

        Then
            HTTP
                status code: {statusCode}
                headers: {headers}

            User info: {responseBody}
     */
    @Test
    void UserRenameOnFail(){

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
            User query

        Then
            HTTP
                status code: 200
                headers:
                    content-type: application/json
                    content-encoding: UTF-8

            User info:
            [
                {
                    "id": 0,
                    "email": "abc@gmail.com",
                    "name": "abc",
                },
                {
                    "id": 1,
                    "email": "def@gmail.com",
                    "name": "def",
                }
            ]
     */
    @Test
    void UserQuery(){

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
            User query

        Then
            HTTP
                statusCode: 401
                headers:
                    content-type: plain/text
                    content-encoding: UTF-8
                responseBody:
                    Can't authenticate who you are.
     */
    @Test
    void UserQueryOnFail(){

    }
}

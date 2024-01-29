import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.web.application.HTTPLoginResponse;
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
import org.web.infrastructure.FileUtil;
import org.web.infrastructure.exceptions.*;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

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
        router.post("/api/users/login", DomainController.class, "login");
        router.patch("/api/users/{userId}", DomainController.class, "rename");
        router.get("/api/users", DomainController.class, "userQuery");

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
        HTTPRequest httpRequest = getHttpRegisterRequest("abc@gmail.com", "abc");

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
        HTTPRequest httpRequest = getHttpRegisterRequest("abc@gmail.com", "abc");

        // When
        httpClient.send(httpRequest);
        HTTPResponse response = httpClient.send(httpRequest);

        // Then
        Assertions.assertEquals(400, response.getHttpStatusCode());
        Assertions.assertEquals("plain/text", response.getHttpHeaders().get("content-type"));
        Assertions.assertEquals("UTF-8", response.getHttpHeaders().get("content-encoding"));
        Assertions.assertEquals("Duplicate email", response.getResponseBody());
    }

    private static HTTPRequest getHttpRegisterRequest(String email, String name) {
        HTTPRequest httpRequest = new HTTPRequest();

        httpRequest.setHttpMethod(HTTPMethod.POST);

        httpRequest.setHttpPath("/api/users");

        Map<String, String> headers = new HashMap<>();
        headers.put("content-type", "application/json");
        httpRequest.setHttpHeaders(headers);

        String password = "hello";
        String body = String.format("""
                {
                    "email": "%s",
                    "name": "%s",
                    "password": "%s"
                }
                 """, email, name, password);
        httpRequest.setBody(body);
        return httpRequest;
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
        webApplication.addException(new IncorrectFormatOfEmailHandler());
        HTTPRequest httpRequest = getHttpRegisterRequest("abc", "abc");

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
        // Given
        httpClient.send(getHttpRegisterRequest("abc@gmail.com", "abc"));

        HTTPRequest httpRequest = getHttpLoginRequest("abc@gmail.com");

        // When
        HTTPResponse response = httpClient.send(httpRequest);

        // Then
        Assertions.assertEquals(200, response.getHttpStatusCode());
        Assertions.assertEquals("application/json", response.getHttpHeaders().get("content-type"));
        Assertions.assertEquals("UTF-8", response.getHttpHeaders().get("content-encoding"));
        HTTPLoginResponse httpLoginResponse = (HTTPLoginResponse) FileUtil.readJsonValue(response.getResponseBody(), HTTPLoginResponse.class);
        Assertions.assertEquals(1, httpLoginResponse.id);
        Assertions.assertEquals("abc", httpLoginResponse.name);
        Assertions.assertEquals("abc@gmail.com", httpLoginResponse.email);
        Assertions.assertNotNull(httpLoginResponse.token);
    }

    private static HTTPRequest getHttpLoginRequest(String email) {
        HTTPRequest httpRequest = new HTTPRequest();

        httpRequest.setHttpMethod(HTTPMethod.POST);

        httpRequest.setHttpPath("/api/users/login");

        Map<String, String> headers = new HashMap<>();
        headers.put("content-type", "application/json");
        httpRequest.setHttpHeaders(headers);

        String password = "hello";
        String body = String.format("""
               {
                   "email": "%s",
                   "password": "%s"
               }
                """, email, password);
        httpRequest.setBody(body);
        return httpRequest;
    }

    /*
        // {email: "def@gmail.com", statusCode: 400, responseBody: Credentials Invalid}
        // {email: "abc", statusCode: 400, responseBody: Login's format incorrect.}
        Given
            HTTP
                headers:
                    content-type: application/json
                method: POST
                path: /api/users

            User info:
            {
                "email": {email},
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
    @ParameterizedTest
    @MethodSource
    void userLoginOnFail(String email, int statusCode, String responseBody, ExceptionHandler<?> exceptionHandler){
        // Given
        webApplication.addException(exceptionHandler);
        httpClient.send(getHttpRegisterRequest("abc@gmail.com", "abc"));

        HTTPRequest httpRequest = getHttpLoginRequest(email);

        // When
        HTTPResponse response = httpClient.send(httpRequest);

        // Then
        Assertions.assertEquals(statusCode, response.getHttpStatusCode());
        Assertions.assertEquals("plain/text", response.getHttpHeaders().get("content-type"));
        Assertions.assertEquals("UTF-8", response.getHttpHeaders().get("content-encoding"));
        Assertions.assertEquals(responseBody, response.getResponseBody());
    }

    private static Stream<Arguments> userLoginOnFail(){
        return Stream.of(
                Arguments.of("def@gmail.com", 400, "Credentials Invalid", new CredentialsInvalidHandler()),
                Arguments.of("abd", 400, "Login's format incorrect.", new IncorrectFormatOfEmailHandler())
        );
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
        // Given
        httpClient.send(getHttpRegisterRequest("abc@gmail.com", "abc"));
        HTTPResponse loginResponse = httpClient.send(getHttpLoginRequest("abc@gmail.com"));
        HTTPLoginResponse httpLoginResponse = (HTTPLoginResponse) FileUtil.readJsonValue(loginResponse.getResponseBody(), HTTPLoginResponse.class);

        HTTPRequest httpRequest = getHttpRenameRequest(httpLoginResponse.token, 1, "newAbc");

        // When
        HTTPResponse response = httpClient.send(httpRequest);

        // Then
        Assertions.assertEquals(204, response.getHttpStatusCode());
    }

    private static HTTPRequest getHttpRenameRequest(String token, int id, String name) {
        HTTPRequest httpRequest = new HTTPRequest();

        httpRequest.setHttpMethod(HTTPMethod.PATCH);

        httpRequest.setHttpPath(String.format("/api/users/%d", id));

        Map<String, String> headers = new HashMap<>();
        headers.put("content-type", "application/json");
        headers.put("Authorization", String.format("Bearer %s", token));
        httpRequest.setHttpHeaders(headers);

        String body = String.format("""
               {
                   "newName": "%s"
               }
                """, name);
        httpRequest.setBody(body);
        return httpRequest;
    }

    /* // {isLegalToken: false, userId: 1, newName: "newAbc", statusCode: 401, responseBody: Can't authenticate who you are.}
       // {isLegalToken: true, userId: 0, newName: "newAbc", statusCode: 403, responseBody: Forbidden}
       // {isLegalToken: true, userId: 1, newName: "hi", statusCode: 400, responseBody: Name's format invalid.}
        Given
            HTTP
                headers:
                    content-type: application/json
                    Authorization: Bearer {token}
                method: PATCH
                path: /api/users/{userId}

            User info:
            {
                "newName": {newName}
            }

        When
            User rename

        Then
            HTTP
                status code: {statusCode}
                headers: {headers}

            User info: {responseBody}
     */
    @ParameterizedTest
    @MethodSource
    void UserRenameOnFail(boolean isLegalToken, int userId, String newName, int statusCode, String responseBody, ExceptionHandler<?> exceptionHandler){
        // Given
        webApplication.addException(exceptionHandler);
        httpClient.send(getHttpRegisterRequest("abc@gmail.com", "abc"));
        HTTPResponse loginResponse = httpClient.send(getHttpLoginRequest("abc@gmail.com"));
        HTTPLoginResponse httpLoginResponse = (HTTPLoginResponse) FileUtil.readJsonValue(loginResponse.getResponseBody(), HTTPLoginResponse.class);
        String token = isLegalToken? httpLoginResponse.token: "";

        HTTPRequest httpRequest = getHttpRenameRequest(token, userId, newName);

        // When
        HTTPResponse response = httpClient.send(httpRequest);

        // Then
        Assertions.assertEquals(statusCode, response.getHttpStatusCode());
        Map<String, String> httpHeaders = response.getHttpHeaders();
        Assertions.assertEquals("plain/text", httpHeaders.get("content-type"));
        Assertions.assertEquals("UTF-8", httpHeaders.get("content-encoding"));
        Assertions.assertEquals(responseBody, response.getResponseBody());
    }

    private static Stream<Arguments> UserRenameOnFail(){
        return Stream.of(
                Arguments.of(false, 1, "newAbc", 401, "Can't authenticate who you are.", new IllegalAuthenticationHandler()),
                Arguments.of(true, 0, "newAbc", 403, "Forbidden", new ForbiddenHandler()),
                Arguments.of(true, 1, "hi", 400, "Name's format invalid.", new InvalidNameFormatHandler())
        );
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
    @ParameterizedTest
    @MethodSource()
    void UserQuery(String queryString, String responseBody){
        // Given
        httpClient.send(getHttpRegisterRequest("abc@gmail.com", "abc"));
        httpClient.send(getHttpRegisterRequest("def@gmail.com", "def"));
        HTTPResponse loginResponse = httpClient.send(getHttpLoginRequest("abc@gmail.com"));
        HTTPLoginResponse httpLoginResponse = (HTTPLoginResponse) FileUtil.readJsonValue(loginResponse.getResponseBody(), HTTPLoginResponse.class);

        HTTPRequest httpRequest = getHttpUserQueryRequest(httpLoginResponse.token, queryString);

        // When
        HTTPResponse response = httpClient.send(httpRequest);

        // Then
        Assertions.assertEquals(200, response.getHttpStatusCode());
        Map<String, String> httpHeaders = response.getHttpHeaders();
        Assertions.assertEquals("application/json", httpHeaders.get("content-type"));
        Assertions.assertEquals("UTF-8", httpHeaders.get("content-encoding"));
        Assertions.assertEquals(responseBody, response.getResponseBody());
    }

    private static HTTPRequest getHttpUserQueryRequest(String token, String queryString) {
        HTTPRequest httpRequest = new HTTPRequest();

        httpRequest.setHttpMethod(HTTPMethod.GET);

        httpRequest.setHttpPath("/api/users");

        Map<String, String> headers = new HashMap<>();
        headers.put("content-type", "application/json");
        headers.put("Authorization", String.format("Bearer %s", token));
        httpRequest.setHttpHeaders(headers);
        httpRequest.setHttpQueryString(queryString);
        return httpRequest;
    }

    private static Stream<Arguments> UserQuery(){
        return Stream.of(
                Arguments.of("keyword=abc", """
                        [{"id":1,"email":"abc@gmail.com","name":"abc"}]"""),
                Arguments.of("", """
                        [{"id":1,"email":"abc@gmail.com","name":"abc"},{"id":2,"email":"def@gmail.com","name":"def"}]""")
        );
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
        // Given
        webApplication.addException(new IllegalAuthenticationHandler());
        httpClient.send(getHttpRegisterRequest("abc@gmail.com", "abc"));
        httpClient.send(getHttpRegisterRequest("def@gmail.com", "def"));

        HTTPRequest httpRequest = getHttpUserQueryRequest("0", "keyword=abc");

        // When
        HTTPResponse response = httpClient.send(httpRequest);

        // Then
        Assertions.assertEquals(401, response.getHttpStatusCode());
        Map<String, String> httpHeaders = response.getHttpHeaders();
        Assertions.assertEquals("plain/text", httpHeaders.get("content-type"));
        Assertions.assertEquals("UTF-8", httpHeaders.get("content-encoding"));
        Assertions.assertEquals("Can't authenticate who you are.", response.getResponseBody());
    }
}

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.web.infrastructure.dto.UserLoginDTO;
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

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class WebAPPTestCase {
    private WebApplication webApplication;

    @BeforeEach
    void setUp(){
        this.webApplication = new WebApplication(8080);

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
        webApplication.launch();
    }
    @AfterEach
    void tearDown(){
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
                "name": "abcabc",
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
                "name": "abcabc"
            }
     */
    @Test
    void UserRegistration() throws IOException, InterruptedException {
        // Given
        HttpClient httpClient = HttpClient.newBuilder().build();
        HttpRequest httpRequest = getHttpRegisterRequest("abc@gmail.com", "abcabc");

        // When
        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        // Then
        Assertions.assertEquals(201, response.statusCode());
        Map<String, List<String>> headers = response.headers().map();
        Assertions.assertEquals("application/json", headers.get("content-type").get(0));
        Assertions.assertEquals("UTF-8", headers.get("content-encoding").get(0));
        Assertions.assertEquals("""
                {"id":1,"email":"abc@gmail.com","name":"abcabc"}""", response.body());
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
                "name": "abcabc",
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
    void UserRegistrationOnDuplicateEmailFail() throws IOException, InterruptedException {
        // Given
        webApplication.addException(new DuplicateEmailHandler());
        HttpClient httpClient = HttpClient.newBuilder().build();
        HttpRequest httpRequest = getHttpRegisterRequest("abc@gmail.com", "abcabc");

        // When
        httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        // Then
        Assertions.assertEquals(400, response.statusCode());
        Map<String, List<String>> headers = response.headers().map();
        Assertions.assertEquals("plain/text", headers.get("content-type").get(0));
        Assertions.assertEquals("UTF-8", headers.get("content-encoding").get(0));
        Assertions.assertEquals("Duplicate email", response.body());
    }

    private static HttpRequest getHttpRegisterRequest(String email, String name) {
        String password = "hello";
        String body = String.format("""
                {
                    "email": "%s",
                    "name": "%s",
                    "password": "%s"
                }
                 """, email, name, password);
        return HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .uri(URI.create("http://127.0.0.1:8080/api/users"))
                .setHeader("content-type", "application/json")
                .build();
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
                "email": "abcabc",
                "name": "abcabc",
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
    void UserRegistrationOnIncorrectFormatOfRegistrationFail() throws IOException, InterruptedException {
        // Given
        HttpClient httpClient = HttpClient.newBuilder().build();
        webApplication.addException(new IncorrectFormatOfEmailHandler());
        HttpRequest httpRequest = getHttpRegisterRequest("abcabc", "abcabc");

        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        // Then
        Assertions.assertEquals(400, response.statusCode());
        Map<String, List<String>> headers = response.headers().map();
        Assertions.assertEquals("plain/text", headers.get("content-type").get(0));
        Assertions.assertEquals("UTF-8", headers.get("content-encoding").get(0));
        Assertions.assertEquals("Registration's format incorrect.", response.body());
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
                "name": "abcabc",
                "token": "67cbbe1b-0c10-46e4-b4a8-9e8505c2453b"
            }
     */
    @Test
    void UserLogin() throws IOException, InterruptedException {
        // Given
        HttpClient httpClient = HttpClient.newBuilder().build();
        httpClient.send(getHttpRegisterRequest("abc@gmail.com", "abcabc"), HttpResponse.BodyHandlers.ofString());

        HttpRequest httpRequest = getHttpLoginRequest("abc@gmail.com");

        // When
        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        // Then
        Assertions.assertEquals(200, response.statusCode());
        Map<String, List<String>> headers = response.headers().map();
        Assertions.assertEquals("application/json", headers.get("content-type").get(0));
        Assertions.assertEquals("UTF-8", headers.get("content-encoding").get(0));
        UserLoginDTO userLoginDTO = (UserLoginDTO) FileUtil.readJsonValue(response.body(), UserLoginDTO.class);
        Assertions.assertEquals(1, userLoginDTO.id);
        Assertions.assertEquals("abcabc", userLoginDTO.name);
        Assertions.assertEquals("abc@gmail.com", userLoginDTO.email);
        Assertions.assertNotNull(userLoginDTO.token);
    }

    private static HttpRequest getHttpLoginRequest(String email) {
        String password = "hello";
        String body = String.format("""
               {
                   "email": "%s",
                   "password": "%s"
               }
                """, email, password);
        return HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .uri(URI.create("http://127.0.0.1:8080/api/users/login"))
                .setHeader("content-type", "application/json")
                .build();
    }

    /*
        // {email: "def@gmail.com", statusCode: 400, responseBody: Credentials Invalid}
        // {email: "abcabc", statusCode: 400, responseBody: Login's format incorrect.}
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
    void userLoginOnFail(String email, int statusCode, String responseBody) throws IOException, InterruptedException {
        // Given
        webApplication.addException(new CredentialsInvalidHandler());
        webApplication.addException(new IncorrectFormatOfEmailHandler());
        HttpClient httpClient = HttpClient.newBuilder().build();
        httpClient.send(getHttpRegisterRequest("abc@gmail.com", "abcabc"), HttpResponse.BodyHandlers.ofString());

        HttpRequest httpRequest = getHttpLoginRequest(email);

        // When
        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        // Then
        Assertions.assertEquals(statusCode, response.statusCode());
        Map<String, List<String>> headers = response.headers().map();
        Assertions.assertEquals("plain/text", headers.get("content-type").get(0));
        Assertions.assertEquals("UTF-8", headers.get("content-encoding").get(0));
        Assertions.assertEquals(responseBody, response.body());
    }

    private static Stream<Arguments> userLoginOnFail(){
        return Stream.of(
                Arguments.of("def@gmail.com", 400, "Credentials Invalid"),
                Arguments.of("abd", 400, "Login's format incorrect.")
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
    void UserRename() throws IOException, InterruptedException {
        // Given
        HttpClient httpClient = HttpClient.newBuilder().build();
        httpClient.send(getHttpRegisterRequest("abc@gmail.com", "abcabc"), HttpResponse.BodyHandlers.ofString());
        HttpResponse<String> loginResponse = httpClient.send(getHttpLoginRequest("abc@gmail.com"), HttpResponse.BodyHandlers.ofString());
        UserLoginDTO userLoginDTO = (UserLoginDTO) FileUtil.readJsonValue(loginResponse.body(), UserLoginDTO.class);

        HttpRequest httpRequest = getHttpRenameRequest(userLoginDTO.token, 1, "newAbc");

        // When
        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        // Then
        Assertions.assertEquals(204, response.statusCode());
    }

    private static HttpRequest getHttpRenameRequest(String token, int id, String name) {
        String body = String.format("""
               {
                   "newName": "%s"
               }
                """, name);
        return HttpRequest.newBuilder()
                .method(HTTPMethod.PATCH.name(), HttpRequest.BodyPublishers.ofString(body))
                .uri(URI.create(String.format("http://127.0.0.1:8080/api/users/%d", id)))
                .header("content-type", "application/json")
                .header("Authorization", String.format("Bearer %s", token))
                .build();
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
    void UserRenameOnFail(boolean isLegalToken, int userId, String newName, int statusCode, String responseBody, ExceptionHandler<?> exceptionHandler) throws IOException, InterruptedException {
        // Given
        webApplication.addException(exceptionHandler);
        HttpClient httpClient = HttpClient.newBuilder().build();
        httpClient.send(getHttpRegisterRequest("abc@gmail.com", "abcabc"), HttpResponse.BodyHandlers.ofString());
        HttpResponse<String> loginResponse = httpClient.send(getHttpLoginRequest("abc@gmail.com"), HttpResponse.BodyHandlers.ofString());
        UserLoginDTO userLoginDTO = (UserLoginDTO) FileUtil.readJsonValue(loginResponse.body(), UserLoginDTO.class);
        String token = isLegalToken? userLoginDTO.token: "";

        HttpRequest httpRequest = getHttpRenameRequest(token, userId, newName);

        // When
        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        // Then
        Assertions.assertEquals(statusCode, response.statusCode());
        Map<String, List<String>> httpHeaders = response.headers().map();
        Assertions.assertEquals("plain/text", httpHeaders.get("content-type").get(0));
        Assertions.assertEquals("UTF-8", httpHeaders.get("content-encoding").get(0));
        Assertions.assertEquals(responseBody, response.body());
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
                    "name": "abcabc",
                },
                {
                    "id": 1,
                    "email": "def@gmail.com",
                    "name": "defdef",
                }
            ]
     */
    @ParameterizedTest
    @MethodSource()
    void UserQuery(String queryString, String responseBody) throws IOException, InterruptedException {
        // Given
        HttpClient httpClient = HttpClient.newBuilder().build();
        httpClient.send(getHttpRegisterRequest("abc@gmail.com", "abcabc"), HttpResponse.BodyHandlers.ofString());
        httpClient.send(getHttpRegisterRequest("def@gmail.com", "defdef"), HttpResponse.BodyHandlers.ofString());
        HttpResponse<String> loginResponse = httpClient.send(getHttpLoginRequest("abc@gmail.com"), HttpResponse.BodyHandlers.ofString());
        UserLoginDTO userLoginDTO = (UserLoginDTO) FileUtil.readJsonValue(loginResponse.body(), UserLoginDTO.class);

        HttpRequest httpRequest = getHttpUserQueryRequest(userLoginDTO.token, queryString);

        // When
        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        // Then
        Assertions.assertEquals(200, response.statusCode());
        Map<String, List<String>> httpHeaders = response.headers().map();
        Assertions.assertEquals("application/json", httpHeaders.get("content-type").get(0));
        Assertions.assertEquals("UTF-8", httpHeaders.get("content-encoding").get(0));
        Assertions.assertEquals(responseBody, response.body());
    }

    private static HttpRequest getHttpUserQueryRequest(String token, String queryString) {
        return HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("http://127.0.0.1:8080/api/users?" + queryString))
                .header("content-type", "application/json")
                .header("Authorization", String.format("Bearer %s", token))
                .build();
    }

    private static Stream<Arguments> UserQuery(){
        return Stream.of(
                Arguments.of("keyword=abcabc", """
                        [{"id":1,"email":"abc@gmail.com","name":"abcabc"}]"""),
                Arguments.of("", """
                        [{"id":1,"email":"abc@gmail.com","name":"abcabc"},{"id":2,"email":"def@gmail.com","name":"defdef"}]""")
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
    void UserQueryOnFail() throws IOException, InterruptedException {
        // Given
        webApplication.addException(new IllegalAuthenticationHandler());
        HttpClient httpClient = HttpClient.newBuilder().build();
        httpClient.send(getHttpRegisterRequest("abc@gmail.com", "abcabc"), HttpResponse.BodyHandlers.ofString());
        httpClient.send(getHttpRegisterRequest("def@gmail.com", "defdef"), HttpResponse.BodyHandlers.ofString());

        HttpRequest httpRequest = getHttpUserQueryRequest("0", "keyword=abc");

        // When
        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        // Then
        Assertions.assertEquals(401, response.statusCode());
        Map<String, List<String>> httpHeaders = response.headers().map();
        Assertions.assertEquals("plain/text", httpHeaders.get("content-type").get(0));
        Assertions.assertEquals("UTF-8", httpHeaders.get("content-encoding").get(0));
        Assertions.assertEquals("Can't authenticate who you are.", response.body());
    }
}

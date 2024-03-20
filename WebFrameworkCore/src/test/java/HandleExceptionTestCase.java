import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.web.domain.core.Container;
import org.web.domain.core.Router;
import org.web.domain.core.WebApplication;
import org.web.domain.ext.PrototypeScope;
import org.web.domain.ext.SingletonScope;
import org.web.domain.ext.protocol.TransformBodyTypeToJsonHandler;
import org.web.domain.ext.protocol.TransformBodyTypeToTextHandler;
import org.web.domain.ext.protocol.TransformBodyTypeToXMLHandler;

import mock.DomainController;
import mock.DomainService;

public class HandleExceptionTestCase {
    private WebApplication webApplication;

    @BeforeEach
    void setUp(){
        this.webApplication = new WebApplication(8080);

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

        webApplication.launch();
    }

    @AfterEach
    void close(){
        webApplication.close();
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
    void notFindPath() throws IOException, InterruptedException {
        // Given

        HttpClient httpClient = HttpClient.newBuilder().build();
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://127.0.0.1:8080/api/notfound"))
                .setHeader("content-type", "application/json")
                .POST(HttpRequest.BodyPublishers.noBody()).build();

        // When
        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        // Then
        Assertions.assertEquals(404, response.statusCode());
        Map<String, List<String>> httpHeaders = response.headers().map();
        Assertions.assertEquals("plain/text", httpHeaders.get("content-type").get(0));
        Assertions.assertEquals("UTF-8", httpHeaders.get("content-encoding").get(0));
        Assertions.assertEquals("Cannot find the path /api/notfound", response.body());
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
    void notAllowedMethod() throws IOException, InterruptedException {
        // Given
        HttpClient httpClient = HttpClient.newBuilder().build();

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .DELETE()
                .uri(URI.create("http://127.0.0.1:8080/api/users"))
                .setHeader("content-type", "application/json").build();

        // When
        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        // Then
        Assertions.assertEquals(405, response.statusCode());
        Map<String, List<String>> httpHeaders = response.headers().map();
        Assertions.assertEquals("plain/text", httpHeaders.get("content-type").get(0));
        Assertions.assertEquals("UTF-8", httpHeaders.get("content-encoding").get(0));
        Assertions.assertEquals("The method DELETE is not allowed on /api/users", response.body());
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
    void notExpectedExecution() throws IOException, InterruptedException {
        // Given
        HttpClient httpClient = HttpClient.newBuilder().build();

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

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .uri(URI.create("http://127.0.0.1:8080/api/users"))
                .header("content-type", "application/json")
                .build();

        // When
        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        // Then
        Assertions.assertEquals(500, response.statusCode());
        Map<String, List<String>> httpHeaders = response.headers().map();
        Assertions.assertEquals("plain/text", httpHeaders.get("content-type").get(0));
        Assertions.assertEquals("UTF-8", httpHeaders.get("content-encoding").get(0));
        Assertions.assertEquals("The exception is not expected", response.body());
    }
}

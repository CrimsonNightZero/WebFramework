import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.web.domain.core.HTTPMethod;
import org.web.domain.core.HTTPRequest;

public class HTTPRequestTestCase {
    /*
        Given
           Path: POST /api/users?a=1 HTTP/1.1

        When
            httpRequest set HttpPath

        Then
            HttpMethod: POST
            HttpPath: /api/users
            HttpQueryVariable: a = 1
            HttpVersion: HTTP/1.1

     */
    @Test
    void parserHttpPath(){
        // Given
        HTTPRequest httpRequest = new HTTPRequest();
        String httpPath = "POST /api/users?a=1 HTTP/1.1";

        // When
        httpRequest.setHttpPath(httpPath);

        // Then
        Assertions.assertEquals(httpRequest.getHttpMethod(), HTTPMethod.POST);
        Assertions.assertEquals(httpRequest.getHttpPath().getPath(), "/api/users");
        Assertions.assertEquals(httpRequest.getHttpQueryVariable().get("a"), "1");
        Assertions.assertEquals(httpRequest.getHttpVersion(), "HTTP/1.1");
    }
}

import mock.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.web.domain.core.HTTPResponse;
import org.web.domain.ext.protocol.TransformBodyTypeToJsonHandler;
import org.web.domain.ext.protocol.TransformBodyTypeToXMLHandler;

import java.util.HashMap;
import java.util.Map;

public class HTTPResponseTestCase {
    /*
        Given
           HTTP
               headers:
                   content-type: application/json

           User info:
           {
               "email": "abc@gmail.com",
               "name": "abc",
               "password": "hello"
           }
           httpResponse set body from user object

        When
           httpResponse getResponseBody

        Then
            HTTP
               status code: 200
               headers:
                   content-type: application/json
               body:
                   {"id":0,"email":"abc@gmail.com","name":"abc","password":"hello"}

     */
    @Test
    void parserJsonBody(){
        // Given
        HTTPResponse httpResponse = new HTTPResponse(200);

        Map<String, String> headers = new HashMap<>();
        headers.put("content-type", "application/json");
        httpResponse.setHttpHeaders(headers);

        String email = "abc@gmail.com";
        String name = "abc";
        String password = "hello";
        User user = new User(0, email, name, password);

        httpResponse.setBody(user);
        httpResponse.setTransformBodyTypeHandler(new TransformBodyTypeToJsonHandler());

        // When
        String responseBody = httpResponse.getResponseBody();

        // Then
        Assertions.assertEquals(200, httpResponse.getHttpStatusCode());
        Map<String, String> httpHeaders = httpResponse.getHttpHeaders();
        Assertions.assertEquals("application/json", httpHeaders.get("content-type"));
        Assertions.assertEquals("""
               {"id":0,"email":"abc@gmail.com","name":"abc","password":"hello"}""", responseBody);
    }


    /*
        Given
           HTTP
               headers:
                   content-type: application/xml

           User info:
           <?xml version="1.0" encoding="UTF-8"?>
           <request>
               <email>abc@gmail.com</email>
               <age>18</age>
               <gender>male</gender>
           </request>

           httpResponse set body from user object
        When
           httpResponse getResponseBody

        Then
            HTTP
               status code: 200
               headers:
                   content-type: application/xml
               body:
                    <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
                    <User>
                        <email>abc@gmail.com</email>
                        <id>0</id>
                        <name>abc</name>
                        <password>hello</password>
                    </User>

     */
    @Test
    void parserXMLBody(){
        // Given
        HTTPResponse httpResponse = new HTTPResponse(200);

        Map<String, String> headers = new HashMap<>();
        headers.put("content-type", "application/xml");
        httpResponse.setHttpHeaders(headers);

        String email = "abc@gmail.com";
        String name = "abc";
        String password = "hello";
        User user = new User(0, email, name, password);
        httpResponse.setBody(user);
        httpResponse.setTransformBodyTypeHandler(new TransformBodyTypeToXMLHandler());

        // When
        String responseBody = httpResponse.getResponseBody();

        // Then
        Assertions.assertEquals(200, httpResponse.getHttpStatusCode());
        Map<String, String> httpHeaders = httpResponse.getHttpHeaders();
        Assertions.assertEquals("application/xml", httpHeaders.get("content-type"));
        Assertions.assertEquals("""
                <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
                <User>
                    <email>abc@gmail.com</email>
                    <id>0</id>
                    <name>abc</name>
                    <password>hello</password>
                </User>
                """, responseBody);
    }
}

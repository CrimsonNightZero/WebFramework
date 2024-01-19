import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.web.domain.core.HTTPRequest;

import java.util.HashMap;
import java.util.Map;

public class HTTPRequestTestCase {
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

        When
            httpRequest readBodyAsObject

        Then
            HTTPPOSTRequest
               email: "abc@gmail.com",
               name: "abc",
               password: "hello"

     */
    @Test
    void parserJsonBody(){
        // Given
        HTTPRequest httpRequest = new HTTPRequest();

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
        HTTPPOSTRequest httpPOSTRequest = httpRequest.readBodyAsObject(HTTPPOSTRequest.class);

        // Then
        Assertions.assertEquals(httpPOSTRequest.email, email);
        Assertions.assertEquals(httpPOSTRequest.name, name);
        Assertions.assertEquals(httpPOSTRequest.password, password);
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

        When
            httpRequest readBodyAsObject

        Then
            HTTPPOSTRequest
               email: "abc@gmail.com",
               name: "abc",
               password: "hello"

     */
    @Test
    void parserXMLBody(){
        // Given
        HTTPRequest httpRequest = new HTTPRequest();

        Map<String, String> headers = new HashMap<>();
        headers.put("content-type", "application/xml");
        httpRequest.setHttpHeaders(headers);

        String email = "abc@gmail.com";
        String name = "abc";
        String password = "hello";
        String body = String.format("""
                <?xml version="1.0" encoding="UTF-8"?>
                <HTTPPOSTRequest>
                    <email>%s</email>
                    <name>%s</name>
                    <password>%s</password>
                </HTTPPOSTRequest>
                """, email, name, password);
        httpRequest.setRequestBody(body);

        // When
        HTTPPOSTRequest httpPOSTRequest = httpRequest.readBodyAsObject(HTTPPOSTRequest.class);

        // Then
        Assertions.assertEquals(httpPOSTRequest.email, email);
        Assertions.assertEquals(httpPOSTRequest.name, name);
        Assertions.assertEquals(httpPOSTRequest.password, password);
    }
}

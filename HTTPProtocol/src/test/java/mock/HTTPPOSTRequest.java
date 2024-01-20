package mock;

import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "mock.HTTPPOSTRequest")
public class HTTPPOSTRequest {
    public String email;
    public String name;
    public String password;
}

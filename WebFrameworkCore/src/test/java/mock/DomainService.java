package mock;

public class DomainService {
    public void validEmail(HTTPPOSTRequest httpPOSTRequest) {
        if (!httpPOSTRequest.email.contains("@")) {
            throw new IllegalArgumentException("Illegal Email");
        }
    }
}

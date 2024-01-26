package org.web.application;

import org.web.domain.User;
import org.web.domain.UserSystem;
import org.web.domain.exceptions.IncorrectFormatOfRegistrationException;

public class DomainService {
    private final UserSystem userSystem;
    public DomainService() {
        this.userSystem = new UserSystem();
    }

    public User registerUser(HTTPPOSTRequest httpPOSTRequest){
        validEmail(httpPOSTRequest);
        userSystem.register(httpPOSTRequest.email, httpPOSTRequest.name, httpPOSTRequest.password);
        return userSystem.query(httpPOSTRequest.name).get(0);
    }

    private void validEmail(HTTPPOSTRequest httpPOSTRequest){
        if (!httpPOSTRequest.email.contains("@")){
            throw new IncorrectFormatOfRegistrationException("Registration's format incorrect.");
        }
    }

    public void getUser(String name){
        userSystem.query();
    }
}

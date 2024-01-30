package org.web.application;

import java.util.List;

import org.web.domain.User;
import org.web.domain.UserSystem;

public class DomainService {
    private final UserSystem userSystem;

    public DomainService() {
        this.userSystem = new UserSystem();
    }

    public User registerUser(HTTPRegisterRequest httpPOSTRequest) {
        userSystem.register(httpPOSTRequest.email, httpPOSTRequest.name, httpPOSTRequest.password);
        return userSystem.query(httpPOSTRequest.name).get(0);
    }

    public User login(HTTPLoginRequest httpLoginRequest) {
        return userSystem.login(httpLoginRequest.email, httpLoginRequest.password);
    }

    public void rename(HTTPRenameRequest httpRenameRequest) {
        userSystem.rename(httpRenameRequest.id, httpRenameRequest.newName);
    }

    public List<User> userQuery() {
        return userSystem.query();
    }

    public List<User> userQuery(String name) {
        return userSystem.query(name);
    }
}

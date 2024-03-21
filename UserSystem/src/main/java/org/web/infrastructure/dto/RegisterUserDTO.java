package org.web.infrastructure.dto;

import org.web.domain.User;
import org.web.domain.core.HTTPResponse;

public class RegisterUserDTO {
    public int id;
    public String email;
    public String name;

    public RegisterUserDTO() {
    }

    public RegisterUserDTO(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.name = user.getName();
    }

    public HTTPResponse response(){
        HTTPResponse httpResponse = new HTTPResponse(201);
        httpResponse.setBody(this);
        return httpResponse;
    }
}

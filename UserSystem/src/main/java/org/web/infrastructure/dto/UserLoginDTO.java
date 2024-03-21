package org.web.infrastructure.dto;

import org.web.domain.User;

public class UserLoginDTO {
    public int id;
    public String email;
    public String name;
    public String token;

    public UserLoginDTO() {
    }

    public UserLoginDTO(User user, String token) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.name = user.getName();
        this.token = token;
    }
}

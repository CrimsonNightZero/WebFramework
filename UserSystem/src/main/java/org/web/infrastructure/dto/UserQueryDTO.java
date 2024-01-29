package org.web.infrastructure.dto;

import org.web.domain.User;

public class UserQueryDTO {
    public int id;
    public String email;
    public String name;

    public UserQueryDTO() {
    }

    public UserQueryDTO(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.name = user.getName();
    }
}

package org.web.application;

import org.web.domain.User;

public class UserQueryDTO {
    private final int id;
    private final String email;
    private String name;

    public UserQueryDTO(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.name = user.getName();
    }

    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

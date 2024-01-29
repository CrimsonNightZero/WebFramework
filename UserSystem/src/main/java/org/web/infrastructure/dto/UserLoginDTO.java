package org.web.infrastructure.dto;

public class UserLoginDTO {
    public int id;
    public String email;
    public String name;
    public String token;

    public UserLoginDTO() {
    }

    public UserLoginDTO(int id, String email, String name, String token) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.token = token;
    }
}

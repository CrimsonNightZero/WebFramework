package org.web.infrastructure.dto;

public class RegisterUserDTO {
    public int id;
    public String email;
    public String name;

    public RegisterUserDTO() {
    }

    public RegisterUserDTO(int id, String email, String name) {
        this.id = id;
        this.email = email;
        this.name = name;
    }
}

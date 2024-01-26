package org.web.application;

import java.util.UUID;

public class HTTPLoginResponse {
    public int id;
    public String email;
    public String name;
    public String token;

    public HTTPLoginResponse() {}

    public HTTPLoginResponse(int id, String email, String name) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.token = String.valueOf(UUID.randomUUID());
    }
}

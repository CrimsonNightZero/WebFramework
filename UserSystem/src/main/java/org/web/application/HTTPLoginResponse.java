package org.web.application;

public class HTTPLoginResponse {
    public int id;
    public String email;
    public String name;
    public String token;

    public HTTPLoginResponse() {}

    public HTTPLoginResponse(int id, String email, String name, String token) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.token = token;
    }
}

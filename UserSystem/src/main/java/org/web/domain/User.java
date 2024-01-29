package org.web.domain;

public class User {
    private final int id;
    private final String email;
    private String name;
    private final String password;

    public User(int id, String email, String name, String password) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.password = password;
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
        if (name.length() < 5 || name.length() > 32){
            throw new IllegalArgumentException("User name length is limited between 5 to 32");
        }
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void formatEmail(){

    }
}

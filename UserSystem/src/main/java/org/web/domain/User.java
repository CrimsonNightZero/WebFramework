package org.web.domain;

import org.web.domain.exceptions.IncorrectFormatOfEmailException;
import org.web.domain.exceptions.InvalidNameFormatException;

public class User {
    private final int id;
    private String email;
    private String name;
    private final String password;

    public User(int id, String email, String name, String password) {
        this.id = id;
        setEmail(email);
        setName(name);
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        validEmail(email);
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        validName(name);
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    private static void validName(String name){
        if (name.length() < 5 || name.length() > 32){
            throw new InvalidNameFormatException("User name length is limited between 5 to 32");
        }
    }

    public static void validEmail(String email){
        if (!email.contains("@")){
            throw new IncorrectFormatOfEmailException("Illegal email.");
        }
    }
}

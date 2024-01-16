package org.web.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class UserSystem {
    private final List<User> users;

    public UserSystem() {
        this.users = new ArrayList<>();
    }

    public void register(String email, String name, String password){
        users.add(new User(users.size() + 1, email, name, password));
    }

    public void login(String email, String password){
        Optional<User> user = getUser(email, password);
    }

    private Optional<User> getUser(String email, String password){
        return users.stream().filter(user -> user.getEmail().equals(email) && user.getPassword().equals(password)).findFirst();
    }

    public void rename(int userId, String name){
        Optional<User> user = getUser(userId);
        user.ifPresent(value -> value.setName(name));
    }

    private Optional<User> getUser(int userId){
        return users.stream().filter(user -> user.getId() == userId).findFirst();
    }

    public List<User> query(){
        return users;
    }

    public List<User> query(String keyword){
        return users.stream().filter(user -> user.getName().equals(keyword)).collect(Collectors.toList());
    }
}

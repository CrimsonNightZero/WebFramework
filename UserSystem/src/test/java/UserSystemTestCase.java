import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.web.domain.User;
import org.web.domain.UserSystem;

import java.util.List;

public class UserSystemTestCase {
    /*
        Given
            User
                email: abc@gmail.com,
                name: abc,
                password: hello

         When
            Register a user

         Then
            User
                id: 1
                email: abc@gmail.com,
                name: abc,
                password: hello
     */
    @Test
    void register(){
        // Given
        String email = "abc@gmail.com";
        String name = "abcabc";
        String password = "hello";
        UserSystem userSystem = new UserSystem();

        // When
        userSystem.register(email, name, password);

        // Then
        User user = userSystem.query(name).get(0);
        Assertions.assertEquals(1, user.getId());
        Assertions.assertEquals(email, user.getEmail());
        Assertions.assertEquals(name, user.getName());
        Assertions.assertEquals(password, user.getPassword());
    }

    /*
        Given
            User
                email: abc@gmail.com,
                name: abc,
                password: hello

         When
            User login

         Then
            User
                id: 1
                email: abc@gmail.com,
                name: abc,
                password: hello
     */
    @Test
    void login(){
        // Given
        String email = "abc@gmail.com";
        String name = "abcabc";
        String password = "hello";
        UserSystem userSystem = new UserSystem();
        userSystem.register(email, name, password);

        // When
        userSystem.login(email, password);

        // Then
        User user = userSystem.query(name).get(0);
        Assertions.assertEquals(1, user.getId());
        Assertions.assertEquals(email, user.getEmail());
        Assertions.assertEquals(name, user.getName());
        Assertions.assertEquals(password, user.getPassword());
    }

    /*
        Given
            User
                name: abc

            User set new name for newAbc

         When
            User rename

         Then
             User
                name: newAbc

     */
    @Test
    void rename(){
        // Given
        String email = "abc@gmail.com";
        String name = "abcabc";
        String password = "hello";
        UserSystem userSystem = new UserSystem();
        userSystem.register(email, name, password);
        User user = userSystem.query(name).get(0);
        String newName = "newAbc";

        // When
        userSystem.rename(user.getId(), newName);

        // Then
        user = userSystem.query(newName).get(0);
        Assertions.assertEquals(1, user.getId());
        Assertions.assertEquals(email, user.getEmail());
        Assertions.assertEquals(newName, user.getName());
        Assertions.assertEquals(password, user.getPassword());
    }

    /*
        Given
            User
                name: abc

         When
            User query

         Then
             User
                id: 1
                email: abc@gmail.com,
                name: abc,
                password: hello

     */
    @Test
    void query(){
        // Given
        String email = "abc@gmail.com";
        String name = "abcabc";
        String password = "hello";
        UserSystem userSystem = new UserSystem();
        userSystem.register(email, name, password);

        // When
        List<User> users = userSystem.query();

        // Then
        User user = users.get(0);
        Assertions.assertEquals(1, users.size());
        Assertions.assertEquals(1, user.getId());
        Assertions.assertEquals(email, user.getEmail());
        Assertions.assertEquals(name, user.getName());
        Assertions.assertEquals(password, user.getPassword());
    }
}

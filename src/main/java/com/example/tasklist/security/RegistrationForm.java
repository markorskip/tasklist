package com.example.tasklist.security;

import com.example.tasklist.model.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import lombok.Data;

@Data
public class RegistrationForm {

    private String username;
    private String password;
    private String name;
    private String email;

    public User toUser(PasswordEncoder passwordEncoder) {
        return new User(
                username, passwordEncoder.encode(password),
                name, email);
    }

}

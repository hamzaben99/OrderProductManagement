package com.example.store.repository;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Repository
public class UserRepository {
    private final static List<UserDetails> APPLICATION_USERS = List.of(
            new User(
                    "administrator",
                    BCrypt.hashpw("password", BCrypt.gensalt()),
                    Collections.singleton(new SimpleGrantedAuthority("ROLE_ADMIN"))
            )
    );

    public UserDetails findUserByUsername(String username) {
        return APPLICATION_USERS
                .stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst()
                .orElseThrow(() -> new UsernameNotFoundException("User "+ username +" not found"));
    }
}

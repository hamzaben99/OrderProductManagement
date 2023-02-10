package com.example.store.service;

import com.example.store.entity.User;
import com.example.store.repository.UserRepository;
import com.example.store.utils.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserDetails loadUserByUsername(String username) {
        return userRepository
                .findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User "+ username +" not found"));
    }

    @Transactional
    public void updateUserPassword(String username, String password, String newPassword) {
        User user = userRepository
                .findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User "+ username +" not found"));
        if (!passwordEncoder.matches(password, user.getPassword()))
            throw new IllegalArgumentException("Incorrect Password");
        if (password.equals(newPassword))
            throw new IllegalArgumentException("New Password must be different than the previous one");

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Transactional
    public void createDefaultAdminIfNotExist() {
        if (!userRepository.existsByUsername("admin")) {
            User user = new User();
            user.setUsername("admin");
            user.setPassword(passwordEncoder.encode("password"));
            user.addRole(Role.ROLE_ADMIN);
            userRepository.save(user);
        }
    }
}

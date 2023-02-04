package com.example.store.service;

import com.example.store.entity.dto.AuthenticationRequest;
import com.example.store.entity.dto.AuthenticationResponse;
import com.example.store.entity.dto.PasswordModificationRequest;
import com.example.store.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    @Autowired
    private UserService userService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtils jwtUtils;

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        UserDetails user = userService.loadUserByUsername(request.getUsername());
        if (user != null) {
            AuthenticationResponse authResponse = new AuthenticationResponse();
            authResponse.setToken(jwtUtils.generateToken(user));
            return authResponse;
        }
        throw new IllegalStateException();
    }

    public void changePassword(PasswordModificationRequest request) {
        userService.updateUserPassword(request.getUsername(), request.getPassword(), request.getNewPassword());
    }
}

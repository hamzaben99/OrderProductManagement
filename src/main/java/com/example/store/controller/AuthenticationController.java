package com.example.store.controller;

import com.example.store.entity.dto.AuthenticationResponse;
import com.example.store.entity.dto.PasswordModificationRequest;
import com.example.store.service.AuthenticationService;
import com.example.store.entity.dto.AuthenticationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/auth")
public class AuthenticationController {
    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(@RequestBody AuthenticationRequest request) {
        AuthenticationResponse response = authenticationService.authenticate(request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/changePassword")
    public ResponseEntity<?> changePassword(@RequestBody PasswordModificationRequest request) {
        authenticationService.changePassword(request);
        return ResponseEntity.ok().build();
    }
}

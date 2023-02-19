package com.example.passwordmanager.controller;

import com.example.passwordmanager.dto.request.LoginRequest;
import com.example.passwordmanager.dto.request.RegisterRequest;
import com.example.passwordmanager.dto.response.AuthenticationResponse;
import com.example.passwordmanager.exception.AuthenticationException;
import com.example.passwordmanager.exception.RegisterException;
import com.example.passwordmanager.service.TokenService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final TokenService service;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request) throws RegisterException {
        return ResponseEntity.ok(service.register(request));
    }
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody LoginRequest request) throws AuthenticationException {
        return ResponseEntity.ok(service.login(request));
    }
    @PostMapping("/refresh")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<AuthenticationResponse> refresh(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        return ResponseEntity.ok(service.refresh(request, response));
    }

}
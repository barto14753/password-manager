package com.example.passwordmanager.controller;

import com.example.passwordmanager.dto.request.auth.LoginRequest;
import com.example.passwordmanager.dto.request.auth.PasswordResetRequest;
import com.example.passwordmanager.dto.request.auth.RegisterRequest;
import com.example.passwordmanager.dto.response.auth.AuthenticationResponse;
import com.example.passwordmanager.exception.AuthException;
import com.example.passwordmanager.exception.password.PasswordException;
import com.example.passwordmanager.exception.password.PasswordResetException;
import com.example.passwordmanager.exception.RegisterException;
import com.example.passwordmanager.service.auth.TokenService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final TokenService service;

    @PostMapping("/register")
    @ApiResponse(
            responseCode = "200",
            description = "User registered"
    )
    @ApiResponse(
            responseCode = "400",
            description = "Invalid user registration details",
            content = @Content(schema = @Schema(hidden = true))
    )
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request)
            throws RegisterException, PasswordException {
        return ResponseEntity.ok(service.register(request));
    }
    @PostMapping("/login")
    @ApiResponse(
            responseCode = "200",
            description = "User logged"
    )
    @ApiResponse(
            responseCode = "400",
            description = "Invalid user credentials",
            content = @Content(schema = @Schema(hidden = true))
    )
    public ResponseEntity<AuthenticationResponse> login(@RequestBody LoginRequest request) throws AuthException {
        return ResponseEntity.ok(service.login(request));
    }
    @PostMapping("/refresh")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<AuthenticationResponse> refresh(HttpServletRequest request, HttpServletResponse response)
            throws AuthException {
        return ResponseEntity.ok(service.refresh(request));
    }

    @PostMapping("/password-reset")
    public ResponseEntity<Object> resetPassword(@RequestBody PasswordResetRequest request)
            throws AuthException, PasswordException, PasswordResetException {
        service.resetPassword(request);
        return ResponseEntity.ok().build();
    }

}
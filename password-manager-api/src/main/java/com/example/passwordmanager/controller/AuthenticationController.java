package com.example.passwordmanager.controller;

import com.example.passwordmanager.dto.request.LoginRequest;
import com.example.passwordmanager.dto.request.RegisterRequest;
import com.example.passwordmanager.dto.response.AuthenticationResponse;
import com.example.passwordmanager.exception.AuthenticationException;
import com.example.passwordmanager.exception.RegisterException;
import com.example.passwordmanager.service.TokenService;
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
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request) throws RegisterException {
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
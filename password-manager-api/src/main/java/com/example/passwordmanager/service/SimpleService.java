package com.example.passwordmanager.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SimpleService {
    private final AuthenticationService authService;

    public String hello() {
        return "Hello " + authService.getAuthentication().getName();
    }
}

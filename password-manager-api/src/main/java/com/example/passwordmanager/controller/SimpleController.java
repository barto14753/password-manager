package com.example.passwordmanager.controller;

import com.example.passwordmanager.service.SimpleService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
public class SimpleController {
    private final SimpleService simpleService;

    @GetMapping("/hello")
    ResponseEntity<String> helloUser() {
        return ResponseEntity.ok().body(simpleService.hello());
    }

    @GetMapping("/admin")
    ResponseEntity<String> bye() {
        return ResponseEntity.ok().body("Hello admin");
    }
}

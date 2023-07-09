package com.example.passwordmanager.controller.password;

import com.example.passwordmanager.dto.request.password.CreatePasswordRequest;
import com.example.passwordmanager.dto.response.password.CreatePasswordResponse;
import com.example.passwordmanager.dto.response.password.GetAllPasswordsResponse;
import com.example.passwordmanager.dto.response.password.GetPasswordResponse;
import com.example.passwordmanager.exception.password.PasswordCreationException;
import com.example.passwordmanager.exception.password.PasswordException;
import com.example.passwordmanager.service.password.PasswordService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/password")
@SecurityRequirement(name = "Bearer Authentication")
@RequiredArgsConstructor
public class PasswordController {
    private final PasswordService passwordService;

    @GetMapping("/{id}")
    ResponseEntity<GetPasswordResponse> getPassword(
            @PathVariable("id") Long id
    ) throws PasswordException {
        return ResponseEntity.ok().body(passwordService.getPasswordResponse(id));
    }

    @GetMapping("/all")
    ResponseEntity<GetAllPasswordsResponse> getAllPasswords() {
        return ResponseEntity.ok().body(passwordService.getAllPasswordsResponse());
    }

    @PostMapping
    ResponseEntity<CreatePasswordResponse> createPassword(@RequestBody CreatePasswordRequest request)
            throws PasswordCreationException {
        return ResponseEntity.ok().body(passwordService.createPasswordResponse(request));
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Object> deletePassword(@PathVariable Long id)
            throws PasswordException {
        passwordService.deletePassword(id);
        return ResponseEntity.ok().build();
    }
}

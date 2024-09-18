package com.barto14753.passwordmanager.controller.password;

import com.barto14753.passwordmanager.dto.request.password.CreatePasswordRequest;
import com.barto14753.passwordmanager.dto.response.password.CreatePasswordResponse;
import com.barto14753.passwordmanager.dto.response.password.GetAllPasswordsResponse;
import com.barto14753.passwordmanager.dto.response.password.GetPasswordResponse;
import com.barto14753.passwordmanager.exception.password.PasswordCreationException;
import com.barto14753.passwordmanager.exception.password.PasswordException;
import com.barto14753.passwordmanager.service.password.PasswordService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.GeneralSecurityException;

@RestController
@RequestMapping("/password")
@SecurityRequirement(name = "Bearer Authentication")
@RequiredArgsConstructor
public class PasswordController {
    private final PasswordService passwordService;

    @GetMapping("/{id}")
    ResponseEntity<GetPasswordResponse> getPassword(
            @PathVariable("id") Long id
    ) throws PasswordException, GeneralSecurityException {
        return ResponseEntity.ok().body(passwordService.getPasswordResponse(id));
    }

    @GetMapping("/all")
    ResponseEntity<GetAllPasswordsResponse> getAllPasswords() {
        return ResponseEntity.ok().body(passwordService.getAllPasswordsResponse());
    }

    @PostMapping
    ResponseEntity<CreatePasswordResponse> createPassword(@RequestBody CreatePasswordRequest request)
            throws PasswordCreationException, GeneralSecurityException {
        return ResponseEntity.ok().body(passwordService.createPasswordResponse(request));
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Object> deletePassword(@PathVariable Long id)
            throws PasswordException {
        passwordService.deletePassword(id);
        return ResponseEntity.ok().build();
    }
}

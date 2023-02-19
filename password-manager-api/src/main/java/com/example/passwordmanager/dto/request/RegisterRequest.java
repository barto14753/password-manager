package com.example.passwordmanager.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    @Schema(description = "First name", type = "string", example = "John")
    private String firstName;

    @Schema(description = "Last name", type = "string", example = "Smith")
    private String lastName;

    @Schema(description = "Email address which needs meet some requirements", type = "string", example = "email@domain.com")
    private String email;

    @Schema(description = "Secret password needs to have minimum 8 characters, contain alpha, numeric and special char", type = "string", example = "Password!123")
    private String password;
}
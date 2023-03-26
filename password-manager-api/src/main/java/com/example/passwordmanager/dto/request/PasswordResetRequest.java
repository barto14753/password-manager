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
public class PasswordResetRequest {
    @Schema(description = "Email address of account", type = "string", example = "email@domain.com")
    private String email;
    @Schema(description = "Old password", type = "string", example = "Password!123")
    private String oldPassword;
    @Schema(description = "Secret password needs to have minimum 8 characters, contain alpha, numeric and special char", type = "string", example = "Password!123")
    private String newPassword;
}

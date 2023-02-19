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
public class LoginRequest {
    @Schema(description = "Email address", type = "string", example = "email@domain.com")
    String email;

    @Schema(description = "Your secret passcode", type = "string", example = "Password!123")
    String password;
}

package com.example.passwordmanager.dto.request.password;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreatePasswordRequest {
    @Schema(description = "Name of password", type = "string", example = "My google account password")
    private String name;

    @Schema(description = "Password value", type = "string", example = "Password!123")
    private String value;
}

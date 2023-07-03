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
public class PatchProfileRequest {
    @Schema(description = "First name", type = "string", example = "John")
    private String firstName;

    @Schema(description = "Last name", type = "string", example = "Smith")
    private String lastName;
}

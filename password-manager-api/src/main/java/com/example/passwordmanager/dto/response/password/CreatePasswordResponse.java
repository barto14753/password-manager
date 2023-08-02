package com.example.passwordmanager.dto.response.password;

import com.example.passwordmanager.dto.util.BasicPassword;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreatePasswordResponse {
    private BasicPassword password;
}

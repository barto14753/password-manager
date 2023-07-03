package com.example.passwordmanager.dto.response;

import com.example.passwordmanager.dto.util.BasicUser;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {
    private BasicUser user;
    private String accessToken;
    private String refreshToken;

}

package com.barto14753.passwordmanager.dto.response.password;

import com.barto14753.passwordmanager.dto.util.BasicPassword;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetAllPasswordsResponse {
    private List<BasicPassword> passwords;
}

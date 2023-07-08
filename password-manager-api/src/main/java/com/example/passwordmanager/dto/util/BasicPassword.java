package com.example.passwordmanager.dto.util;

import com.example.passwordmanager.model.Password;
import com.example.passwordmanager.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BasicPassword {
    private Long id;
    private String name;
    private String value;
    private Long ownerId;

    public BasicPassword(Password password) {
        this.id = password.getId();
        this.name = password.getName();
        this.value = password.getValue();
        this.ownerId = password.getOwner().getId();
    }
}

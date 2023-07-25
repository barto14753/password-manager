package com.example.passwordmanager.dto.util;

import com.example.passwordmanager.crypto.Encrypter;
import com.example.passwordmanager.model.Password;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.security.GeneralSecurityException;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BasicPassword {
    private Long id;
    private String name;
    private String decryptedValue;
    private Long ownerId;
    private Long created;
    private Long modified;

    public BasicPassword(Password password, String decryptedValue) {
        this.id = password.getId();
        this.name = password.getName();
        this.decryptedValue = decryptedValue;
        this.ownerId = password.getOwner().getId();
        this.created = password.getCreated();
        this.modified = password.getModified();
    }
}

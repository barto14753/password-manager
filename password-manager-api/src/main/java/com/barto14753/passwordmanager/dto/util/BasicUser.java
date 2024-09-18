package com.barto14753.passwordmanager.dto.util;

import com.barto14753.passwordmanager.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BasicUser {
    private String email;
    private String firstName;
    private String lastName;

    public BasicUser(User user) {
        this.email = user.getEmail();
        this.firstName = user.getFirstname();
        this.lastName = user.getLastname();
    }
}

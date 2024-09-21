package com.barto14753.passwordmanager.dto.response.kdbx;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class KDBXResponse {
    private Long id;
    private Long owner_id;
    private String name;
    private String password;
    private boolean open;
    private String created;
}

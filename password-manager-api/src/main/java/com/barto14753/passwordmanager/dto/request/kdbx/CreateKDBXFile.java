package com.barto14753.passwordmanager.dto.request.kdbx;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateKDBXFile {
    private String name;
    private String password;
    private byte[] data;
}

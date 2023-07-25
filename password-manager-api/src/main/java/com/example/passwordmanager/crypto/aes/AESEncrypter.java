package com.example.passwordmanager.crypto.aes;

import com.example.passwordmanager.crypto.Encrypter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.crypto.*;
import java.security.GeneralSecurityException;
import java.util.Base64;

@Component
@RequiredArgsConstructor
public class AESEncrypter implements Encrypter {
    private final SecretKey secretKey;
    private final Cipher cipher;

    public String encrypt(String data) throws GeneralSecurityException {
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedData = cipher.doFinal(data.getBytes());
        return Base64.getEncoder().encodeToString(encryptedData);
    }

    public String decrypt(String encryptedData) throws GeneralSecurityException {
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decryptedData = cipher.doFinal(Base64.getDecoder().decode(encryptedData));
        return new String(decryptedData);
    }
}

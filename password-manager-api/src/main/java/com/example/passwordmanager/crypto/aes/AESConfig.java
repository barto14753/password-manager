package com.example.passwordmanager.crypto.aes;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;

@Configuration
public class AESConfig {
    @Bean
    public Cipher cipher() throws NoSuchPaddingException, NoSuchAlgorithmException {
        return Cipher.getInstance("AES");
    }
    @Bean
    public SecretKey secretKey() throws NoSuchAlgorithmException {
        // Generate a 128-bit AES key (change if needed)
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(128);
        return keyGen.generateKey();
    }
}

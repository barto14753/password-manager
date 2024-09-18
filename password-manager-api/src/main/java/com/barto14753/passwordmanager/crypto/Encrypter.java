package com.barto14753.passwordmanager.crypto;

import java.security.GeneralSecurityException;

public interface Encrypter {
    public String encrypt(String textToEncrypt) throws GeneralSecurityException;
    public String decrypt(String encryptedText) throws GeneralSecurityException;
}

package com.moinchallenge.service;

import com.moinchallenge.config.EncryptionProperties;
import org.junit.jupiter.api.Test;

import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;

class EncryptionServiceTest {
    @Test
    public void testEncryptionDecryption() {
        String secretKey = Base64.getEncoder().encodeToString("0123456789abcdef".getBytes());
        EncryptionProperties properties = new EncryptionProperties(secretKey);
        EncryptionService encryptionService = new EncryptionService(properties);

        String plainText = "test1234";
        String cipherText = encryptionService.encrypt(plainText);
        assertNotNull(cipherText);
        assertNotEquals(plainText, cipherText);

        String decryptedText = encryptionService.decrypt(cipherText);
        assertEquals(plainText, decryptedText);
    }
}
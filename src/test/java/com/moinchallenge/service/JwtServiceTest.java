package com.moinchallenge.service;

import com.moinchallenge.config.JwtProperties;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    @Test
    public void testGenerateAndValidateToken() {
        String secretKey = "0123456789abcdef0123456789abcdef";
        String base64Key = java.util.Base64.getEncoder().encodeToString(secretKey.getBytes());
        long expiration = 30 * 60 * 1000L; // 30분
        JwtProperties jwtProperties = new JwtProperties(base64Key, expiration);
        JwtService jwtService = new JwtService(jwtProperties);

        String userId = "test@test.com";
        String token = jwtService.generateToken(userId);
        assertNotNull(token);

        boolean valid = jwtService.validateToken(token);
        assertTrue(valid);

        String extractedUserId = jwtService.getUserIdFromToken(token);
        assertEquals(userId, extractedUserId);
    }

    @Test
    public void testValidateInvalidToken() {
        String secretKey = "0123456789abcdef0123456789abcdef";
        String base64Key = java.util.Base64.getEncoder().encodeToString(secretKey.getBytes());
        long expiration = 30 * 60 * 1000L;
        JwtProperties jwtProperties = new JwtProperties(base64Key, expiration);
        JwtService jwtService = new JwtService(jwtProperties);

        String invalidToken = "nonPassed.token.value";
        boolean valid = jwtService.validateToken(invalidToken);
        assertFalse(valid);
    }
}
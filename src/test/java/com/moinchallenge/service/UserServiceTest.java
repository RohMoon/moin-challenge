package com.moinchallenge.service;

import com.moinchallenge.constant.IdType;
import com.moinchallenge.dto.request.SignRequest;
import com.moinchallenge.entity.User;
import com.moinchallenge.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Mock
    private EncryptionService encryptionService;

    @Mock
    private JwtService jwtService;

    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userService = new UserService(userRepository, passwordEncoder, encryptionService, jwtService);
    }

    @Test
    void testSignupSuccess() {
        SignRequest request = new SignRequest("test@example.com", "password", "Test User", "REG_NO", "123456-1234567");

        when(userRepository.existsByUserId("tester@test.com")).thenReturn(false);

        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");

        when(encryptionService.encrypt("123456-1234567")).thenReturn("encryptedIdValue");

        User savedUser = User.builder()
                .id(1L)
                .userId("test@example.com")
                .password("encodedPassword")
                .name("Test User")
                .idType(IdType.RESIDENT_REGISTRATION_NUMBER)
                .idValue("encryptedIdValue")
                .build();
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        assertDoesNotThrow(() -> userService.signup(request));

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testSignupMissingField() {
        SignRequest request = new SignRequest(null, "password", "Test User", "REG_NO", "123456-1234567");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> userService.signup(request));
        assertEquals("잘못된 파라미터 입니다.", exception.getMessage());
    }

    @Test
    void testSignupDuplicateUser() {
        SignRequest request = new SignRequest("test@tester.com", "password", "Tester Roh", "REG_NO", "910722-1234567");

        when(userRepository.existsByUserId("test@tester.com")).thenReturn(true);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> userService.signup(request));
        assertEquals("이미 존재하는 유저 입니다.", exception.getMessage());
    }

    @Test
    void testLoginSuccess() {
        String userId = "test@tester.com";
        String rawPassword = "password";
        String encodedPassword = "encodedPassword";

        User user = User.builder()
                .id(1L)
                .userId(userId)
                .password(encodedPassword)
                .build();

        when(userRepository.findByUserId(userId)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(rawPassword, encodedPassword)).thenReturn(true);
        when(jwtService.generateToken(userId)).thenReturn("generatedToken");

        String token = userService.login(userId, rawPassword);

        assertEquals("generatedToken", token);
    }

    @Test
    void testLoginUserNotFound() {
        String userId = "test@tester.com";
        when(userRepository.findByUserId(userId)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> userService.login(userId, "password"));
        assertEquals("존재하지 않는 사용자입니다.", exception.getMessage());
    }

    @Test
    void testLoginInvalidPassword() {
        String userId = "test@tester.com";
        String rawPassword = "noPassedPassword";
        String encodedPassword = "encodedPassword";

        User user = User.builder()
                .id(1L)
                .userId(userId)
                .password(encodedPassword)
                .build();

        when(userRepository.findByUserId(userId)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(rawPassword, encodedPassword)).thenReturn(false);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> userService.login(userId, rawPassword));
        assertEquals("비밀번호가 올바르지 않습니다.", exception.getMessage());
    }
}
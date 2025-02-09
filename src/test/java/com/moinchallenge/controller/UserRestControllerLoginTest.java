package com.moinchallenge.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moinchallenge.config.GlobalExceptionHandler;
import com.moinchallenge.dto.request.LoginRequest;
import com.moinchallenge.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class UserRestControllerLoginTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserRestController userRestController;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(userRestController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    @DisplayName("로그인 성공 - JWT 토큰 발급")
    void login_success() throws Exception {
        // given
        LoginRequest loginRequest = new LoginRequest("testUser@gmail.com", "P@ssw0rd");
        String requestBody = objectMapper.writeValueAsString(loginRequest);

        // 가정: userService.login()이 "fake-jwt-token" 반환
        when(userService.login(loginRequest.getUserId(), loginRequest.getPassword()))
                .thenReturn("fake-jwt-token");

        // when / then
        mockMvc.perform(post("/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultCode").value(200))
                .andExpect(jsonPath("$.resultMsg").value("OK"))
                .andExpect(jsonPath("$.token").value("fake-jwt-token"));
    }

    @Test
    @DisplayName("로그인 실패 - 존재하지 않는 사용자 (IllegalArgumentException)")
    void login_fail_user_not_found() throws Exception {
        // given
        LoginRequest loginRequest = new LoginRequest("notfound@gmail.com", "somePassword");
        String requestBody = objectMapper.writeValueAsString(loginRequest);

        // userService.login()이 IllegalArgumentException을 던진다고 가정
        doThrow(new IllegalArgumentException("존재하지 않는 사용자입니다."))
                .when(userService).login(loginRequest.getUserId(), loginRequest.getPassword());

        // when / then
        mockMvc.perform(post("/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest()) // 400
                .andExpect(jsonPath("$.resultCode").value(400))
                .andExpect(jsonPath("$.resultMsg").value("존재하지 않는 사용자입니다."));
    }

    @Test
    @DisplayName("로그인 실패 - 비밀번호 불일치 (IllegalArgumentException)")
    void login_fail_wrong_password() throws Exception {
        // given
        LoginRequest loginRequest = new LoginRequest("testUser@gmail.com", "wrongPassword");
        String requestBody = objectMapper.writeValueAsString(loginRequest);

        doThrow(new IllegalArgumentException("비밀번호가 올바르지 않습니다."))
                .when(userService).login(loginRequest.getUserId(), loginRequest.getPassword());

        // when / then
        mockMvc.perform(post("/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest()) // 400
                .andExpect(jsonPath("$.resultCode").value(400))
                .andExpect(jsonPath("$.resultMsg").value("비밀번호가 올바르지 않습니다."));
    }

    @Test
    @DisplayName("로그인 실패 - 서버 내부 오류")
    void login_fail_server_error() throws Exception {
        // given
        LoginRequest loginRequest = new LoginRequest("testUser@gmail.com", "somePassword");
        String requestBody = objectMapper.writeValueAsString(loginRequest);

        doThrow(new RuntimeException("DB 연결 오류"))
                .when(userService).login(loginRequest.getUserId(), loginRequest.getPassword());

        // when / then
        mockMvc.perform(post("/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isInternalServerError()) // 500
                .andExpect(jsonPath("$.resultCode").value(500))
                .andExpect(jsonPath("$.resultMsg").value("서버 에러가 발생했습니다"));
    }

    @Test
    @DisplayName("로그인 실패 - 유효성 검증 (userId 누락)")
    void login_fail_validation() throws Exception {
        // given
        // userId = null
        LoginRequest loginRequest = new LoginRequest(null, "somePassword");
        String requestBody = objectMapper.writeValueAsString(loginRequest);

        // when / then
        mockMvc.perform(post("/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.resultCode").value(400))
                // GlobalExceptionHandler가 MethodArgumentNotValidException 처리 시 첫 번째 에러 메시지 등
                .andExpect(jsonPath("$.resultMsg").value("아이디를 입력해야 합니다."));
    }
}
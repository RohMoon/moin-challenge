package com.moinchallenge.controller;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.moinchallenge.config.GlobalExceptionHandler;
import com.moinchallenge.controller.UserRestController;
import com.moinchallenge.dto.request.SignRequest;
import com.moinchallenge.repository.UserRepository;
import com.moinchallenge.service.EncryptionService;
import com.moinchallenge.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class UserRestControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserRestController userRestController;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(userRestController)
                .setControllerAdvice(new GlobalExceptionHandler()) // ✅ 예외 핸들러 추가
                .build();
    }

    @Test
    @DisplayName("singUp_Success")
    void signup_success() throws Exception {
        // given
        SignRequest signRequest = new SignRequest(
                "test11@gmail.com",
                "P@&&w0rd!1",
                "tester",
                "REG_NO",
                "910722-1199871"
        );
        String requestBody = new ObjectMapper().writeValueAsString(signRequest);

        // then
        mockMvc.perform(post("/user/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk()) // 200 OK
                .andExpect(jsonPath("$.resultCode").value(200))
                .andExpect(jsonPath("$.resultMsg").value("OK"));
    }

    @Test
    @DisplayName("회원가입 실패 - 이메일 형식 오류")
    void signup_fail_invalid_email() throws Exception {
        // given
        SignRequest signRequest = new SignRequest(
                "invalid-email",
                "P@&&w0rd!1",
                "tester",
                "REG_NO",
                "910722-1199871"
        );
        String requestBody = new ObjectMapper().writeValueAsString(signRequest);

        // then
        mockMvc.perform(post("/user/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest()) // ✅ 400 Bad Request 반환
                .andExpect(jsonPath("$.resultCode").value(400))
                .andExpect(jsonPath("$.resultMsg").value("유효하지 않은 이메일 형식입니다.")); // ✅ 메시지 검증
    }

    @Test
    @DisplayName("회원가입 실패 - 잘못된 파라미터")
    void signup_fail_invalid_params() throws Exception {
        // given
        SignRequest signRequest = new SignRequest(
                null, // userId 없음
                "P@&&w0rd!1",
                "tester",
                "REG_NO",
                "910722-1199871"
        );
        String requestBody = new ObjectMapper().writeValueAsString(signRequest);

        // then
        mockMvc.perform(post("/user/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest()) // ✅ 400 Bad Request 기대
                .andExpect(jsonPath("$.resultCode").value(400)) // ✅ `GlobalExceptionHandler`에서 반환하는 값 확인
                .andExpect(jsonPath("$.resultMsg").value("이메일을 입력해야 합니다.")); // ✅ 첫 번째 오류 메시지 검증
    }

    @Test
    @DisplayName("회원가입 실패 - 서버 오류")
    void signup_fail_server_error() throws Exception {
        // given
        SignRequest signRequest = new SignRequest(
                "test@gmail.com",
                "P@&&w0rd!1",
                "tester",
                "REG_NO",
                "910722-1199871"
        );
        String requestBody = new ObjectMapper().writeValueAsString(signRequest);

        // when (서버 내부 에러 발생 시 doThrow 사용)
        doThrow(new RuntimeException("서버 에러가 발생했습니다."))
                .when(userService).signup(signRequest);

        // then
        mockMvc.perform(post("/user/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isInternalServerError()) // 500 Internal Server Error
                .andExpect(jsonPath("$.resultCode").value(500))
                .andExpect(jsonPath("$.resultMsg").value("서버 에러가 발생했습니다."));
    }
}


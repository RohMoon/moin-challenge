package com.moinchallenge.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moinchallenge.dto.request.LoginRequest;
import com.moinchallenge.dto.request.SignRequest;
import com.moinchallenge.service.JwtService;
import com.moinchallenge.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UserRestController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private JwtService jwtService;

    @Test
    public void testSignup_Success() throws Exception {
        SignRequest request = new SignRequest(
                "test@tester.com",
                "Password@123",
                "Test User",
                "REG_NO",
                "1234567890"
        );

        mockMvc.perform(post("/user/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultCode").value(200))
                .andExpect(jsonPath("$.resultMsg").value("OK"));
    }

    @Test
    public void testLogin_Success() throws Exception {
        String mockToken = "mocked_jwt_token";

        Mockito.when(userService.login(any(String.class), any(String.class)))
                .thenReturn(mockToken);

        LoginRequest request = new LoginRequest("test@example.com", "Password@123");

        mockMvc.perform(post("/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultCode").value(200))
                .andExpect(jsonPath("$.resultMsg").value("OK"))
                .andExpect(jsonPath("$.token").value(mockToken));
    }
}
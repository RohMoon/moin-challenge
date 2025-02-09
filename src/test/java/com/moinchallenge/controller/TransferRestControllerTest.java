package com.moinchallenge.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moinchallenge.config.GlobalExceptionHandler;
import com.moinchallenge.dto.request.QuoteRequest;
import com.moinchallenge.dto.response.QuoteResponse;
import com.moinchallenge.service.QuoteService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class TransferRestControllerTest {
    @Mock
    private QuoteService quoteService;

    @InjectMocks
    private TransferRestController transferRestController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(transferRestController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
        objectMapper = new ObjectMapper();
    }

    @Test
    @DisplayName("견적 생성 - 성공 케이스(200 OK)")
    void getQuote_success() throws Exception {
        // given
        QuoteRequest quoteRequest = new QuoteRequest(10000, "USD");
        String requestJson = objectMapper.writeValueAsString(quoteRequest);

        QuoteResponse mockResponse = new QuoteResponse("quoteId-123", 1300.5, "2025-02-08 12:00:00", 70.0);
        when(quoteService.calculateQuote(any(QuoteRequest.class)))
                .thenReturn(mockResponse);

        // when & then
        mockMvc.perform(post("/transfer/quote")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk()) // 200
                .andExpect(jsonPath("$.resultCode").value(200))
                .andExpect(jsonPath("$.resultMsg").value("OK"))
                .andExpect(jsonPath("$.data.quoteId").value("quoteId-123"))
                .andExpect(jsonPath("$.data.exchangeRate").value(1300.5))
                .andExpect(jsonPath("$.data.targetAmount").value(70.0))
                .andExpect(jsonPath("$.data.expireTime").value("2025-02-08 12:00:00"));

        verify(quoteService, times(1)).calculateQuote(any(QuoteRequest.class));
    }

    @Test
    @DisplayName("견적 생성 - 잘못된 파라미터(IllegalArgumentException → 400)")
    void getQuote_badRequest() throws Exception {
        // given
        QuoteRequest quoteRequest = new QuoteRequest(1, "USD");
        String requestJson = objectMapper.writeValueAsString(quoteRequest);

        doThrow(new IllegalArgumentException("송금액은 음수가 될 수 없습니다."))
                .when(quoteService).calculateQuote(any(QuoteRequest.class));

        // when & then
        mockMvc.perform(post("/transfer/quote")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest()) // 400
                .andExpect(jsonPath("$.resultCode").value(400))
                .andExpect(jsonPath("$.resultMsg").value("송금액은 음수가 될 수 없습니다."));

        verify(quoteService, times(1)).calculateQuote(any(QuoteRequest.class));
    }

    @Test
    @DisplayName("견적 생성 - 서버 내부 예외(RuntimeException → 500)")
    void getQuote_serverError() throws Exception {
        // given
        QuoteRequest quoteRequest = new QuoteRequest(10000, "USD");
        String requestJson = objectMapper.writeValueAsString(quoteRequest);

        doThrow(new RuntimeException("DB 연결 오류"))
                .when(quoteService).calculateQuote(any(QuoteRequest.class));

        // when & then
        mockMvc.perform(post("/transfer/quote")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isInternalServerError()) // 500
                .andExpect(jsonPath("$.resultCode").value(500))
                .andExpect(jsonPath("$.resultMsg").value("알 수 없는 에러 입니다."));

        verify(quoteService, times(1)).calculateQuote(any(QuoteRequest.class));
    }
}
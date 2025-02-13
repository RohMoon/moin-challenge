package com.moinchallenge.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moinchallenge.config.GlobalExceptionHandler;
import com.moinchallenge.dto.request.QuoteRequest;
import com.moinchallenge.dto.response.QuoteResponse;
import com.moinchallenge.dto.response.QuoteWrapperResponse;
import com.moinchallenge.service.JwtService;
import com.moinchallenge.service.QuoteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(QuoteRestController.class)
@AutoConfigureMockMvc(addFilters = false)
class QuoteRestControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private QuoteService quoteService;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private JwtService jwtService;

        @Test
        public void testGetQuote_Success () throws Exception {

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            QuoteResponse quoteResponse = QuoteResponse.builder()
                    .quoteId(1L)
                    .exchangeRate(9.013)
                    .expireTime(LocalDateTime.now().plusDays(3).truncatedTo(ChronoUnit.MINUTES))
                    .targetAmount(630.91)
                    .build();

            Mockito.when(quoteService.calculateQuote(any(QuoteRequest.class))).thenReturn(quoteResponse);

            QuoteRequest request = new QuoteRequest(10000L, "JPY");

            mockMvc.perform(post("/transfer/quote")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.resultCode").value(200))
                    .andExpect(jsonPath("$.resultMsg").value("OK"))
                    .andExpect(jsonPath("$.quote.quoteId").value("1"))
                    .andExpect(jsonPath("$.quote.exchangeRate").value(9.013))
                    .andExpect(jsonPath("$.quote.expireTime").value( LocalDateTime.now().plusDays(3).truncatedTo(ChronoUnit.MINUTES).format(formatter)))
                    .andExpect(jsonPath("$.quote.targetAmount").value(630.91));
        }
    }
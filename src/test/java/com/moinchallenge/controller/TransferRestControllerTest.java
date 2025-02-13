package com.moinchallenge.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moinchallenge.dto.request.TransferRequest;
import com.moinchallenge.dto.response.TransferListResponse;
import com.moinchallenge.service.JwtService;
import com.moinchallenge.service.TransferHistoryService;
import com.moinchallenge.service.TransferService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TransferRestController.class)
@AutoConfigureMockMvc(addFilters = false)
public class TransferRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private TransferService transferService;

    @MockBean
    private TransferHistoryService transferHistoryService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testRequestTransfer_Success() throws Exception {
        TransferRequest request = new TransferRequest(1L);

        mockMvc.perform(post("/transfer/request")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultCode").value(200))
                .andExpect(jsonPath("$.resultMsg").value("OK"));
    }

    @Test
    public void testGetTransferHistory_Success() throws Exception {
        TransferListResponse transferListResponse = new TransferListResponse(
                "test@example.com",
                "Test User",
                3,
                1000.0,
                null
        );

        Mockito.when(transferHistoryService.getTransferHistory()).thenReturn(transferListResponse);

        mockMvc.perform(get("/transfer/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultCode").value(200))
                .andExpect(jsonPath("$.resultMsg").value("OK"))
                .andExpect(jsonPath("$.userId").value("test@example.com"))
                .andExpect(jsonPath("$.name").value("Test User"))
                .andExpect(jsonPath("$.todayTransferCount").value(3))
                .andExpect(jsonPath("$.todayTransferUsdAmount").value(1000.0));
    }
}

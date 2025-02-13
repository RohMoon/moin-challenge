package com.moinchallenge.service;

import com.moinchallenge.entity.Quote;
import com.moinchallenge.repository.TransferHistoryRepository;
import com.moinchallenge.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.time.ZoneId;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

class TransferHistoryServiceTest {

    @Mock
    private TransferHistoryRepository transferHistoryRepository;

    @Mock
    private UserRepository userRepository;

    private TransferHistoryService transferHistoryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        transferHistoryService = new TransferHistoryService(transferHistoryRepository, userRepository);
    }

    @Test
    void testCalculateDailySum() {
        Long userPk = 1L;
        double repositorySum = 100.0;
        double quoteTargetAmount = 50.0;
        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Seoul"));

        Quote quote = Quote.builder()
                .targetAmount(quoteTargetAmount)
                .build();

        when(transferHistoryRepository.sumAmountByIdAndTransferDateTime(eq(userPk), any(LocalDateTime.class)))
                .thenReturn(repositorySum);

        double result = transferHistoryService.calculateDailySum(userPk, quote, now);

        assertEquals(repositorySum + quoteTargetAmount, result, 0.001);
    }

}
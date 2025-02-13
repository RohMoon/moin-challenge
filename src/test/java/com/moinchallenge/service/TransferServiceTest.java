package com.moinchallenge.service;

import com.moinchallenge.constant.IdType;
import com.moinchallenge.entity.Quote;
import com.moinchallenge.entity.User;
import com.moinchallenge.repository.QuoteRepository;
import com.moinchallenge.repository.UserRepository;
import com.moinchallenge.utils.SecurityUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TransferServiceTest {

    @Mock
    private QuoteRepository quoteRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TransferHistoryService transferHistoryService;

    private TransferService transferService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        transferService = new TransferService(quoteRepository, userRepository, transferHistoryService);
    }

    @Test
    void testRequestTransfer_Success() {
        Long quoteId = 1L;
        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Seoul"));

        Quote quote = Quote.builder()
                .Id(quoteId)
                .expireTime(now.plusMinutes(5))
                .targetAmount(100.0)
                .build();
        User user = User.builder()
                .id(1L)
                .userId("tester@test.com")
                .idType(IdType.RESIDENT_REGISTRATION_NUMBER)
                .build();

        when(quoteRepository.findById(quoteId)).thenReturn(Optional.of(quote));
        try (MockedStatic<SecurityUtil> util = org.mockito.Mockito.mockStatic(SecurityUtil.class)) {
            util.when(SecurityUtil::getCurrentUserId).thenReturn("tester@test.com");
            when(userRepository.findByUserId("tester@test.com")).thenReturn(Optional.of(user));
            when(transferHistoryService.calculateDailySum(eq(user.getId()), eq(quote), any(LocalDateTime.class)))
                    .thenReturn(400.0);

            assertDoesNotThrow(() -> transferService.requestTransfer(quoteId));
            verify(transferHistoryService, times(1))
                    .saveTransferHistory(eq(user), eq(quote), any(LocalDateTime.class));
        }
    }

    @Test
    void testRequestTransfer_QuoteNotFound() {
        Long quoteId = 1L;
        when(quoteRepository.findById(quoteId)).thenReturn(Optional.empty());
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> transferService.requestTransfer(quoteId));
        assertEquals("유효하지 않은 견적서입니다.", exception.getMessage());
    }

    @Test
    void testRequestTransfer_QuoteExpired() {
        Long quoteId = 1L;
        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Seoul"));

        Quote expiredQuote = Quote.builder()
                .Id(quoteId)
                .expireTime(now.minusMinutes(1))
                .build();

        when(quoteRepository.findById(quoteId)).thenReturn(Optional.of(expiredQuote));
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> transferService.requestTransfer(quoteId));
        assertEquals("견적서가 만료 되었습니다.", exception.getMessage());
    }

    @Test
    void testRequestTransfer_UserNotFound() {
        Long quoteId = 1L;
        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Seoul"));

        Quote quote = Quote.builder()
                .Id(quoteId)
                .expireTime(now.plusMinutes(5))
                .targetAmount(100.0)
                .build();

        when(quoteRepository.findById(quoteId)).thenReturn(Optional.of(quote));

        try (MockedStatic<SecurityUtil> util = org.mockito.Mockito.mockStatic(SecurityUtil.class)) {
            util.when(SecurityUtil::getCurrentUserId).thenReturn("noExist@noExist.com");
            when(userRepository.findByUserId("noExist@noExist.com")).thenReturn(Optional.empty());
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> transferService.requestTransfer(quoteId));
            assertEquals("존재하지 않는 사용자입니다.", exception.getMessage());
        }
    }

    @Test
    void testRequestTransfer_LimitExceeded() {
        Long quoteId = 1L;
        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Seoul"));

        Quote quote = Quote.builder()
                .Id(quoteId)
                .expireTime(now.plusMinutes(5))
                .targetAmount(600.0)
                .build();

        when(quoteRepository.findById(quoteId)).thenReturn(Optional.of(quote));

        try (MockedStatic<SecurityUtil> util = org.mockito.Mockito.mockStatic(SecurityUtil.class)) {
            util.when(SecurityUtil::getCurrentUserId).thenReturn("tester@test.com");
            User dummyUser = User.builder()
                    .id(1L)
                    .userId("tester@test.com")
                    .idType(IdType.RESIDENT_REGISTRATION_NUMBER)
                    .build();

            when(userRepository.findByUserId("tester@test.com")).thenReturn(Optional.of(dummyUser));
            when(transferHistoryService.calculateDailySum(eq(dummyUser.getId()), eq(quote), any(LocalDateTime.class)))
                    .thenReturn(1100.0);

            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> transferService.requestTransfer(quoteId));
            assertEquals("오늘 송금 한도를 초과하였습니다.", exception.getMessage());
        }
    }
}
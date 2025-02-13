package com.moinchallenge.entity;

import com.moinchallenge.constant.Currency;
import com.moinchallenge.exception.NegativeNumberException;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class QuoteTest {

    @Test
    void testCreateQuote_USD_UnderOneMillion() {
        long amount = 500_000L;
        Currency targetCurrency = Currency.USD;
        double exchangeRate = 1300.0;
        String userId = "test@example.com";

        Quote quote = Quote.create(amount, targetCurrency, exchangeRate, userId);

        long expectedFee = 2000L;
        long expectedSourceAmount = amount;
        double net = amount - expectedFee;
        double expectedTargetAmount = net / exchangeRate;
        expectedTargetAmount = Math.round(expectedTargetAmount * 100.0) / 100.0;

        assertEquals(userId, quote.getUserId());
        assertEquals(expectedSourceAmount, quote.getSourceAmount());
        assertEquals(exchangeRate, quote.getExchangeRate());
        assertEquals(expectedTargetAmount, quote.getTargetAmount(), 0.001);

        assertNotNull(quote.getExpireTime());
        assertTrue(quote.getExpireTime().isAfter(LocalDateTime.now()));
    }

    @Test
    void testCreateQuote_USD_OverOneMillion() {
        long amount = 2_000_000L;
        Currency targetCurrency = Currency.USD;
        double exchangeRate = 1300.0;
        String userId = "test@example.com";

        Quote quote = Quote.create(amount, targetCurrency, exchangeRate, userId);

        long expectedFee = 5000L;
        double net = amount - expectedFee;
        double expectedTargetAmount = net / exchangeRate;
        expectedTargetAmount = Math.round(expectedTargetAmount * 100.0) / 100.0;

        assertEquals(userId, quote.getUserId());
        assertEquals(amount, quote.getSourceAmount());
        assertEquals(exchangeRate, quote.getExchangeRate());
        assertEquals(expectedTargetAmount, quote.getTargetAmount(), 0.001);
    }

    @Test
    void testCreateQuote_JPY() {
        long amount = 100_000L;
        Currency targetCurrency = Currency.JPY;
        double exchangeRate = 9.0;
        String userId = "test@example.com";

        Quote quote = Quote.create(amount, targetCurrency, exchangeRate, userId);

        long expectedFee = 3500L;
        double net = amount - expectedFee;
        double expectedTargetAmount = net / exchangeRate;
        expectedTargetAmount = Math.round(expectedTargetAmount);

        assertEquals(userId, quote.getUserId());
        assertEquals(amount, quote.getSourceAmount());
        assertEquals(exchangeRate, quote.getExchangeRate());
        assertEquals(expectedTargetAmount, quote.getTargetAmount(), 0.001);
    }

    @Test
    void testCreateQuote_InvalidAmount() {
        long amount = 0L;
        Currency targetCurrency = Currency.USD;
        double exchangeRate = 1300.0;
        String userId = "test@example.com";

        NegativeNumberException exception = assertThrows(NegativeNumberException.class,
                () -> Quote.create(amount, targetCurrency, exchangeRate, userId));
        assertEquals("송금액은 음수가 될 수 없습니다.", exception.getMessage());
    }

    @Test
    void testCreateQuote_NetAmountNegative() {
        long amount = 1L;
        Currency targetCurrency = Currency.USD;
        double exchangeRate = 1300.0;
        String userId = "test@example.com";

        NegativeNumberException exception = assertThrows(NegativeNumberException.class,
                () -> Quote.create(amount, targetCurrency, exchangeRate, userId));
        assertEquals("송금액은 음수가 될 수 없습니다.", exception.getMessage());
    }

    @Test
    void testCreateQuote_UnsupportedCurrency() {
        long amount = 500_000L;
        Currency unsupportedCurrency = Currency.KRW;
        double exchangeRate = 1.0;
        String userId = "test@example.com";

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> Quote.create(amount, unsupportedCurrency, exchangeRate, userId));
        assertEquals("지원하지 않는 통화입니다.", exception.getMessage());
    }
}
package com.moinchallenge.entity;

import com.moinchallenge.constant.Currency;
import com.moinchallenge.exception.NegativeNumberException;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Quote {            // 저장없이 REDIS 저장 고려 

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    private String userId;
    private LocalDateTime expireTime;

    @Enumerated(EnumType.STRING)
    private Currency currency;
    private Long sourceAmount;
    private Double exchangeRate;
    private Double targetAmount;

    public long getFee() {
        return calculateFee(this.sourceAmount, this.currency);
    }

    public boolean isExpired(LocalDateTime now) {
        return this.expireTime.isBefore(now);
    }

    public static Quote create(
            long amount,
            Currency targetCurrency,
            double exchangeRate,
            String userId
    ) {

        QuoteValidator.validAmountIsPositive(amount);

        long fee = calculateFee(amount, targetCurrency);

        double netRemittanceAmount = amount - fee;
        QuoteValidator.validNetAmountIsPositive(netRemittanceAmount);

        double targetAmount = calculateTargetAmount(exchangeRate, netRemittanceAmount);
        QuoteValidator.validTargetAmountIsPositive(targetAmount);

        targetAmount = roundTo(targetAmount, targetCurrency.getFractionDigits());

        LocalDateTime expireAt = calculateExpireTime();

        return Quote.builder()
                .userId(userId)
                .expireTime(expireAt)
                .sourceAmount(amount)
                .exchangeRate(exchangeRate)
                .targetAmount(targetAmount)
                .currency(targetCurrency)
                .build();
    }

    private static LocalDateTime calculateExpireTime() {
        return LocalDateTime.now(ZoneId.of("Asia/Seoul")).plusMinutes(10);
    }

    private static double calculateTargetAmount(double exchangeRate, double net) {
        return net / exchangeRate;
    }

    private static double roundTo(double value, int fractionDigits) {
        double scale = Math.pow(10, fractionDigits);
        return Math.round(value * scale) / scale;
    }

    private static long calculateFee(long amount, Currency targetCurrency) {
        if (Currency.USD == targetCurrency) {
            if (amount <= 1_000_000) {
                return (long) (amount * 0.002 + 1_000);
            } else {
                return (long) (amount * 0.001 + 3_000);
            }
        } else if (Currency.JPY == targetCurrency) {
            // 고정 3000, 수수료율 0.5%
            return (long) (amount * 0.005 + 3_000);
        } else {
            throw new IllegalArgumentException("지원하지 않는 통화입니다.");
        }
    }
    private static class QuoteValidator {

        private static void validAmountIsPositive(long amount) {
            if (amount <= 0) {
                throw new NegativeNumberException("송금액은 음수가 될 수 없습니다.");
            }
        }

        private static void validNetAmountIsPositive(double netAmount) {
            if (netAmount < 0) {
                throw new NegativeNumberException("수수료를 제외한 송금액이 0보다 작을 수 없습니다.");
            }
        }

        private static void validTargetAmountIsPositive(double receivedAmount) {
            if (receivedAmount < 0) {
                throw new IllegalArgumentException("수취 금액이 0보다 작을 수 없습니다.");
            }
        }

        private static void validCurrencyIsNotNull(Currency targetCurrency) {
            if (targetCurrency == null) {
                throw new IllegalArgumentException("올바른 통화를 선택해야 합니다.");
            }
        }
    }

}

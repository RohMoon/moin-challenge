package com.moinchallenge.entity;

import com.moinchallenge.constant.Currency;
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

        // 1) 금액 검증 (양의 정수만 가능)
        QuoteValidator.validAmountIsPositive(amount);

        // 2) 수수료 계산 (각 통화별 로직)
        // USD
        // 1~1,000,000 => 1000원 + 0.2%
        // 1,000,000 초과 => 3000원 + 0.1%
        // JPY => 고정 3000, 0.5%
        long fee = calculateFee(amount, targetCurrency);

        // 3) 순액(net) = amount - fee
        double netRemittanceAmount = amount - fee;
        QuoteValidator.validNetAmountIsPositive(netRemittanceAmount);

        // 5) 수취 금액 = net / exchangeRate
        double targetAmount = calculateTargetAmount(exchangeRate, netRemittanceAmount);
        QuoteValidator.validTargetAmountIsPositive(targetAmount);

        // 4) 환율 가져오기
        // e.g. targetCurrency= "JPY" => "FRX.KRWJPY"

        // 6) 소수점 자리수 → Java Util Currency defaultFractionDigits 적용
        // 예: USD => 2자리, JPY => 0자리 ...
        // 그냥 예시로서, 실무에서는 Currency.getInstance("JPY").getDefaultFractionDigits()
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

    /**
     * 통화별 수수료 계산
     */
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

    /**
     * **검증 로직을 캡슐화한 내부 클래스**
     * - `Quote` 내부에서만 사용 가능하도록 `private static`으로 설정
     */
    private static class QuoteValidator {
        private static void validAmountIsPositive(long amount) {
            if (amount <= 0) {
                throw new IllegalArgumentException("송금액은 0 또는 음수가 될 수 없습니다.");
            }
        }

        private static void validNetAmountIsPositive(double netAmount) {
            if (netAmount < 0) {
                throw new IllegalArgumentException("수수료를 제외한 송금액이 0보다 작을 수 없습니다.");
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

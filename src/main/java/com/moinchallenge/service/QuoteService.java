package com.moinchallenge.service;

import com.moinchallenge.dto.request.QuoteRequest;
import com.moinchallenge.dto.response.QuoteResponse;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class QuoteService {
    private final ExchangeRateService exchangeRateService;

    public QuoteService(ExchangeRateService exchangeRateService) {
        this.exchangeRateService = exchangeRateService;
    }

    public QuoteResponse calculateQuote(QuoteRequest req) {
        //TODO :  금액한도설정필요
        int amount = req.getAmount();
        String targetCurrency = req.getTargetCurrency();

        // 1) 금액 검증 (양의 정수만 가능)
        if (amount <= 0) {
            // 과제 요구사항: "송금액은 음수가 될 수 없습니다." → IllegalArgumentException
            throw new IllegalArgumentException("송금액은 음수가 될 수 없습니다.");
        }

        // 2) 수수료 계산 (각 통화별 로직)
        // USD
        // 1~1,000,000 => 1000원 + 0.2%
        // 1,000,000 초과 => 3000원 + 0.1%
        // JPY => 고정 3000, 0.5%
        double fee = calculateFee(amount, targetCurrency);

        // 3) 순액(net) = amount - fee
        double net = amount - fee;
        if (net < 0) {
            throw new IllegalArgumentException("송금액은 음수가 될 수 없습니다.");
        }

        // 4) 환율 가져오기
        // e.g. targetCurrency= "JPY" => "FRX.KRWJPY"
        double exchangeRate = exchangeRateService.getExchangeRate("KRW", targetCurrency);

        // 5) 수취 금액 = net / exchangeRate
        double targetAmount = net / exchangeRate;
        if (targetAmount < 0) {
            throw new IllegalArgumentException("송금액은 음수가 될 수 없습니다.");
        }

        // 6) 소수점 자리수 → Java Util Currency defaultFractionDigits 적용
        // 예: USD => 2자리, JPY => 0자리 ...
        // 그냥 예시로서, 실무에서는 Currency.getInstance("JPY").getDefaultFractionDigits()
        int fractionDigits = exchangeRateService.getFractionDigits(targetCurrency);

        targetAmount = roundTo(targetAmount, fractionDigits);

        // 7) 만료시간 = 현재 + 10분
        LocalDateTime expireAt = LocalDateTime.now().plusMinutes(10);
        String expireTime = expireAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        // 8) quoteId 임의 생성 (ex. UUID나 DB seq)
        String quoteId = String.valueOf(System.currentTimeMillis());

        return QuoteResponse.of(
                quoteId,
                exchangeRate,
                expireTime,
                targetAmount
        );
    }

    /**
     * 통화별 수수료 계산
     */
    private double calculateFee(int amount, String targetCurrency) {
        if ("USD".equalsIgnoreCase(targetCurrency)) {
            if (amount <= 1_000_000) {
                // fixed=1000, rate=0.2%
                return amount * 0.002 + 1000;
            } else {
                // fixed=3000, rate=0.1%
                return amount * 0.001 + 3000;
            }
        } else if ("JPY".equalsIgnoreCase(targetCurrency)) {
            // 고정 3000, 수수료율 0.5%
            return amount * 0.005 + 3000;
        } else {
            // 과제에서는 USD, JPY만 예시지만 확장 시 여기 추가
            throw new IllegalArgumentException("지원하지 않는 통화입니다.");
        }
    }

    private double roundTo(double value, int fractionDigits) {
        double scale = Math.pow(10, fractionDigits);
        return Math.round(value * scale) / scale;
    }
}

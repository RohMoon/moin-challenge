package com.moinchallenge.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.moinchallenge.entity.Quote;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class QuoteResponse {
    private Long quoteId;
    private Double exchangeRate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime expireTime;
    private Double targetAmount;
    private String targetCurrency;

    public static QuoteResponse from(Quote quote) {
        return QuoteResponse.builder()
                .quoteId(quote.getId())
                .exchangeRate(quote.getExchangeRate())
                .expireTime(quote.getExpireTime())
                .targetAmount(quote.getTargetAmount())
                .targetCurrency(quote.getCurrency().getCode())
                .build();
    }
}

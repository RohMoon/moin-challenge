package com.moinchallenge.dto.response;

import com.moinchallenge.entity.Quote;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class QuoteResponse {
    private Long quoteId;
    private Double exchangeRate;
    private String expireTime;
    private Double targetAmount;

    public static QuoteResponse of(
            Long quoteId,
            Double exchangeRate,
            String expireTime,
            Double targetAmount
    ) {
        return new QuoteResponse(quoteId, exchangeRate, expireTime, targetAmount);
    }

    public static QuoteResponse from(Quote quote) {
        return QuoteResponse.builder()
                .quoteId(quote.getId())
                .exchangeRate(quote.getExchangeRate())
                .expireTime(quote.getExpireTime().toString())
                .targetAmount(quote.getTargetAmount())
                .build();
    }
}

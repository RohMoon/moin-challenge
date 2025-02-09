package com.moinchallenge.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class QuoteResponse {
    private String quoteId;
    private Double exchangeRate;
    private String expireTime;
    private Double targetAmount;

    public static QuoteResponse of(
            String quoteId,
            Double exchangeRate,
            String expireTime,
            Double targetAmount
    ) {
        return new QuoteResponse(quoteId, exchangeRate, expireTime, targetAmount);
    }
}

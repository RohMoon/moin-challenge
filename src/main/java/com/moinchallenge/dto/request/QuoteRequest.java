package com.moinchallenge.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class QuoteRequest {
    @NotNull(message = "송금 금액을 입력해야 합니다.")
    @Positive(message = "송금액은 1원 이상이어야 합니다.")
    private long amount;

    @NotNull(message = "목표 통화를 입력해야 합니다.")
    private String targetCurrency;
}

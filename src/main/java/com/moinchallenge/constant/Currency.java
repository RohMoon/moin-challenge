package com.moinchallenge.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Currency {
    USD("USD", "달러", "미국"),
    JPY("JPY", "엔", "일본"),
    KRW("KRW", "원", "한국");

    private final String code;
    private final String title;
    private final String country;

    public static Currency fromCode(String code) {
        for (Currency currency : values()) {
            if (currency.code.equalsIgnoreCase(code))
                return currency;
        }
        throw new IllegalArgumentException("알 수 없는 통화 코드 입니다. " + code);
    }

    public int getFractionDigits() {
        try {
            return java.util.Currency.getInstance(this.getCode()).getDefaultFractionDigits();
        } catch (Exception exception) {
            return 2;
        }
    }
}

package com.moinchallenge.dto.response;

import lombok.*;

@Getter
@Builder(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class QuoteWrapperResponse extends BaseResponse {
    private QuoteResponse quote;

    protected QuoteWrapperResponse(int resultCode, String resultMsg, QuoteResponse quote) {
        super(resultCode,resultMsg);
        this.quote = quote;
    }


    public static QuoteWrapperResponse of(int resultCode, String resultMsg, QuoteResponse quote) {
        return new QuoteWrapperResponse(resultCode, resultMsg, quote);
    }
}

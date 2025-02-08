package com.moinchallenge.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class ApiResponse {
    private int resultCode;
    private String resultMsg;

    public static ApiResponse of(int resultCode, String resultMsg) {
        return new ApiResponse(resultCode, resultMsg);
    }
}

package com.moinchallenge.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class LoginResponse {
    private int resultCode;
    private String resultMsg;
    private String token;

    public static LoginResponse of(int resultCode, String resultMsg, String token) {
        return new LoginResponse(resultCode, resultMsg, token);
    }
}

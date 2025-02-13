package com.moinchallenge.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class LoginResponse extends BaseResponse{
    private String token;

    protected LoginResponse(int resultCode, String resultMsg, String token) {
        super(resultCode, resultMsg);
        this.token = token;
    }

    public static LoginResponse of(int resultCode, String resultMsg, String token) {
        return new LoginResponse(resultCode, resultMsg, token);
    }
}

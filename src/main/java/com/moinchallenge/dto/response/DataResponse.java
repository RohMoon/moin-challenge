package com.moinchallenge.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class DataResponse<T> {
    private int resultCode;
    private String resultMsg;
    private T data;

    public static <T> DataResponse<T> of(int resultCode, String resultMsg, T data) {
        return new DataResponse<>(resultCode, resultMsg, data);
    }
}

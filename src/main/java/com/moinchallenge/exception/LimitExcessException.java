package com.moinchallenge.exception;

import lombok.Getter;

@Getter
public class LimitExcessException extends RuntimeException {
    private final String errorCode;

    public LimitExcessException(String message) {
        super(message);
        this.errorCode = "LIMIT_EXCESS";
    }
}

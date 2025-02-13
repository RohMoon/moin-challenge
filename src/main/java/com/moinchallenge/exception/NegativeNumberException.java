package com.moinchallenge.exception;

import lombok.Getter;

@Getter
public class NegativeNumberException extends RuntimeException {
    private final String errorCode;

    public NegativeNumberException(String message) {
        super(message);
        this.errorCode = "NEGATIVE_NUMBER";
    }
}

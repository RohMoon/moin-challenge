package com.moinchallenge.config;

import com.moinchallenge.dto.response.BaseResponse;
import com.moinchallenge.exception.LimitExcessException;
import com.moinchallenge.exception.NegativeNumberException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BaseResponse> handleValidationExceptions(MethodArgumentNotValidException exception) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(BaseResponse.of(HttpStatus.BAD_REQUEST.value(), "잘못된 파라미터 입니다."));
    }

    @ResponseBody
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<BaseResponse> handleValidationExceptions(IllegalArgumentException exception) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(BaseResponse.of(HttpStatus.BAD_REQUEST.value(), exception.getMessage()));
    }

    @ResponseBody
    @ExceptionHandler(NegativeNumberException.class)
    public ResponseEntity<BaseResponse> handleNegativeNumberException(NegativeNumberException exception) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(BaseResponse.of(HttpStatus.BAD_REQUEST.value(), "송금액은 음수가 될 수 없습니다."));
    }

    @ResponseBody
    @ExceptionHandler(LimitExcessException.class)
    public ResponseEntity<BaseResponse> handleLimitExcessException(LimitExcessException exception) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(BaseResponse.of(HttpStatus.BAD_REQUEST.value(), "오늘 송금 한도 초과 입니다."));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<BaseResponse> handleAllException(Exception exception) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(BaseResponse.of(HttpStatus.INTERNAL_SERVER_ERROR.value(), "알 수 없는 에러 입니다."));
    }
}
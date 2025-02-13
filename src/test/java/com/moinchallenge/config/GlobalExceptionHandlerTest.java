package com.moinchallenge.config;

import com.moinchallenge.dto.response.BaseResponse;
import com.moinchallenge.exception.NegativeNumberException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Collections;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GlobalExceptionHandlerTest {

    @Test
    void handleValidationExceptions() {
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError fieldError = new FieldError("testObjectError" ,"field", "잘못된 파라미터 입니다.");
        when(bindingResult.getFieldErrors()).thenReturn( Collections.singletonList(fieldError));

        MethodArgumentNotValidException exception = new MethodArgumentNotValidException(null, bindingResult);

        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        ResponseEntity<BaseResponse> responseEntity = handler.handleValidationExceptions(exception);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        BaseResponse body = responseEntity.getBody();
        assertNotNull(body);
        assertEquals(400, body.getResultCode());
        assertEquals("잘못된 파라미터 입니다.", body.getResultMsg());
    }

    @Test
    void handleIllegalArgumentException() {
        IllegalArgumentException ex = new IllegalArgumentException("테스트 오류");
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        ResponseEntity<BaseResponse> responseEntity = handler.handleValidationExceptions(ex);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        BaseResponse body = responseEntity.getBody();
        assertNotNull(body);
        assertEquals(400, body.getResultCode());
        assertEquals("테스트 오류", body.getResultMsg());
    }

    @Test
    void handleNegativeNumberException() {
        NegativeNumberException ex = new NegativeNumberException("음수는 허용되지 않습니다.");
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        ResponseEntity<BaseResponse> responseEntity = handler.handleNegativeNumberException(ex);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        BaseResponse body = responseEntity.getBody();
        assertNotNull(body);
        assertEquals(400, body.getResultCode());
        assertEquals("음수는 허용되지 않습니다.", body.getResultMsg());
    }

    @Test
    void handleGeneralException() {
        Exception ex = new Exception("일반 예외");
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        ResponseEntity<BaseResponse> responseEntity = handler.handleAllException(ex);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        BaseResponse body = responseEntity.getBody();
        assertNotNull(body);
        assertEquals(500, body.getResultCode());
        assertEquals("알 수 없는 에러 입니다.", body.getResultMsg());
    }
}
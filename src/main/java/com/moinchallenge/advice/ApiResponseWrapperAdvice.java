package com.moinchallenge.advice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moinchallenge.dto.response.ApiResponse;
import com.moinchallenge.dto.response.DataResponse;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@ControllerAdvice
public class ApiResponseWrapperAdvice implements ResponseBodyAdvice<Object> {
    private static final int SUCCESS_STATUS = HttpStatus.OK.value();
    private static final String SUCCESS_MESSAGE = "OK";

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        Class<?> clazz = returnType.getParameterType();
        return !(ApiResponse.class.isAssignableFrom(clazz))
                || DataResponse.class.isAssignableFrom(clazz);
    }

    @Override
    public Object beforeBodyWrite(Object body,
                                  MethodParameter returnType,
                                  MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request, ServerHttpResponse response) {
        if (selectedConverterType == StringHttpMessageConverter.class) {
            try {
                return new ObjectMapper().writeValueAsString(DataResponse.of(SUCCESS_STATUS, SUCCESS_MESSAGE, body));
            } catch (Exception exception) {
                throw new RuntimeException("응답 문자열 변환 실패", exception);
            }
        }
        return DataResponse.of(SUCCESS_STATUS, SUCCESS_MESSAGE, body);
    }
}

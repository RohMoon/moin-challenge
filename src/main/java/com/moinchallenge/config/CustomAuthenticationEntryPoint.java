package com.moinchallenge.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moinchallenge.dto.response.BaseResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {

        HttpStatus httpStatus = HttpStatus.UNAUTHORIZED;
        BaseResponse baseResponse = BaseResponse.of(httpStatus.value(), "사용할 수 없는 토큰입니다.");

        response.setStatus(httpStatus.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE + "; charset=UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(baseResponse));
    }
}

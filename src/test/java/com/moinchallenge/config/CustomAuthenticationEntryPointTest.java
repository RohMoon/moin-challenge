package com.moinchallenge.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moinchallenge.dto.response.BaseResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.AuthenticationException;

import static org.junit.jupiter.api.Assertions.*;

class CustomAuthenticationEntryPointTest {
    @Test
    public void testCommence() throws Exception {
        CustomAuthenticationEntryPoint entryPoint = new CustomAuthenticationEntryPoint();
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        AuthenticationException authException = new AuthenticationException("Auth failed") {};

        entryPoint.commence(request, response, authException);

        assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatus());
        assertEquals("application/json; charset=UTF-8", response.getContentType());

        ObjectMapper objectMapper = new ObjectMapper();
        BaseResponse baseResponse = objectMapper.readValue(response.getContentAsString(), BaseResponse.class);
        assertEquals(401, baseResponse.getResultCode());
        assertEquals("사용할 수 없는 토큰입니다.", baseResponse.getResultMsg());
    }
}
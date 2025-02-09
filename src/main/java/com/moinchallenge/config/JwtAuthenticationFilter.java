package com.moinchallenge.config;

import com.moinchallenge.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        // 1) 요청 헤더에서 "Authorization" 추출
        String token = getTokenFromRequest(request);

        // 2) 유효한 토큰이라면 -> 인증 정보 SecurityContext 에 세팅
        if (token != null && jwtService.validateToken(token)) {
            String userId = jwtService.getUserIdFromToken(token);

            // 스프링 시큐리티가 사용하는 인증 객체
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(userId, null, List.of());

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        // 3) 다음 필터 진행
        filterChain.doFilter(request, response);
    }
}

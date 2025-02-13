package com.moinchallenge.controller;

import com.moinchallenge.dto.request.LoginRequest;
import com.moinchallenge.dto.request.SignRequest;
import com.moinchallenge.dto.response.ApiResponse;
import com.moinchallenge.dto.response.LoginResponse;
import com.moinchallenge.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("user")
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class UserRestController {
    private final UserService userService;

    @Operation(
            summary = "회원 가입 API"
    )
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse> signup(@RequestBody @Valid SignRequest signRequest) {
        try {
            userService.signup(signRequest);

            return ResponseEntity.ok().body(
                    ApiResponse.of(200, "OK")
            );
        } catch (IllegalArgumentException exception) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(
                            ApiResponse.of(400, "잘못된 파라미터 입니다.")
                    );
        } catch (Exception exception) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(
                            ApiResponse.of(500, "서버 에러가 발생했습니다.")
                    );
        }
    }

    @Operation(
            summary = "로그인 API"
    )
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest loginRequest) {
        try {
            String token = userService.login(
                    loginRequest.getUserId(),
                    loginRequest.getPassword()
            );

            return ResponseEntity.ok()
                    .body(
                            LoginResponse.of(200, "OK", token)
                    );
        } catch (IllegalArgumentException exception) {
            return ResponseEntity.badRequest()
                    .body(
                            LoginResponse.of(400, exception.getMessage(), null)
                    );
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(
                            LoginResponse.of(500, "서버 에러가 발생했습니다", null)
                    );
        }
    }
}

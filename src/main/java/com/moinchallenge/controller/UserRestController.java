package com.moinchallenge.controller;

import com.moinchallenge.dto.request.LoginRequest;
import com.moinchallenge.dto.request.SignRequest;
import com.moinchallenge.dto.response.BaseResponse;
import com.moinchallenge.dto.response.LoginResponse;
import com.moinchallenge.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
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
    public ResponseEntity<BaseResponse> signup(@RequestBody @Valid SignRequest signRequest) {
        userService.signup(signRequest);
        return ResponseEntity.ok()
                .body(BaseResponse.of(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase()));
    }

    @Operation(
            summary = "로그인 API"
    )
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest loginRequest) {
        String token = userService.login(
                loginRequest.getUserId(),
                loginRequest.getPassword()
        );

        return ResponseEntity.ok()
                .body(LoginResponse.of(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), token));
    }
}

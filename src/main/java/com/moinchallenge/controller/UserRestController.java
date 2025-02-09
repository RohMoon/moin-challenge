package com.moinchallenge.controller;

import com.moinchallenge.dto.request.SignRequest;
import com.moinchallenge.dto.response.ApiResponse;
import com.moinchallenge.service.UserService;
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

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse> signup(@RequestBody @Valid SignRequest signRequest) {
        System.out.println("✅ UserRestController - signup() 실행됨");
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
}

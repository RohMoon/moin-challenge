package com.moinchallenge.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LoginRequest {
    @NotBlank(message = "아이디를 입력해야 합니다.")
    private String userId;

    @NotBlank(message = "비밀번호를 입력해야 합니다.")
    private String password;
}

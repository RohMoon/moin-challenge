package com.moinchallenge.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class SignRequest {

    @NotBlank(message = "이메일을 입력해야 합니다.")
    @Email(message = "유효하지 않은 이메일 형식입니다.")
    private String userId;

    @NotBlank(message = "비밀번호를 입력해야 합니다.")
    private String password;//단방향 암호화 대상
    
    @NotBlank(message = "이름을 입력해야 합니다.")
    private String name;

    @NotBlank(message = "ID 유형을 입력해야 합니다.")
    @Pattern(regexp = "REG_NO|BUSINESS_NO",
            message = "ID 유형은 'REG_NO', 'BUSINESS_NO' 만 가능합니다.")
    private String idType;

    @NotBlank(message = "주민등록번호 혹은 사업자번호를 입력해야 합니다.")
    private String idValue; //암호화 대상
}

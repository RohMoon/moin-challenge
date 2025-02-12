package com.moinchallenge.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum IdType {
    RESIDENT_REGISTRATION_NUMBER("REG_NO", "주민등록번호", 1000),
    BUSINESS_REGISTRATION_NUMBER("BUSINESS_NO", "사업자번호", 5000);

    private final String code;
    private final String title;
    private final double limitAmount;

    public static IdType fromCode(String code) {
        return switch (code.toUpperCase()) {
            case "REG_NO" -> RESIDENT_REGISTRATION_NUMBER;
            case "BUSINESS_NO" -> BUSINESS_REGISTRATION_NUMBER;
            default -> throw new IllegalArgumentException("알 수 없는 식별 코드입니다: " + code);
        };
    }

    public static boolean isPersonal(IdType idType) {
        return idType == IdType.RESIDENT_REGISTRATION_NUMBER;
    }
}

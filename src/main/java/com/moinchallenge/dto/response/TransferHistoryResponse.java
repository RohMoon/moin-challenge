package com.moinchallenge.dto.response;

import com.moinchallenge.entity.TransferHistory;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.format.DateTimeFormatter;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class TransferHistoryResponse {
    private Long sourceAmount;       // 원화 송금 요청액
    private Long fee;                // 송금 수수료
    private Double usdExchangeRate;  // USD 환율
    private Double usdAmount;        // USD 송금액
    private String targetCurrency;   // 받는 통화 (코드)
    private Double exchangeRate;     // 송금 시 적용된 환율
    private Double targetAmount;     // 수취 금액
    private String requestedDate;    // 송금 요청 시간 (문자열)

    public static TransferHistoryResponse from(TransferHistory transferHistory) {
        return new TransferHistoryResponse(
                transferHistory.getSourceAmount(),
                transferHistory.getFee(),
                transferHistory.getUsdExchangeRate(),
                transferHistory.getUsdAmount(),
                transferHistory.getTargetCurrency().getCode(),
                transferHistory.getExchangeRate(),
                transferHistory.getTargetAmount(),
                transferHistory.getRequestedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        );
    }
}

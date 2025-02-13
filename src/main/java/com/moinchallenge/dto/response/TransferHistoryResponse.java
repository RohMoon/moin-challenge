package com.moinchallenge.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.moinchallenge.entity.TransferHistory;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class TransferHistoryResponse {
    private Long sourceAmount;
    private Long fee;
    private Double usdExchangeRate;
    private Double usdAmount;
    private String targetCurrency;
    private Double exchangeRate;
    private Double targetAmount;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime requestedDate;

    public static TransferHistoryResponse from(TransferHistory transferHistory) {
        return new TransferHistoryResponse(
                transferHistory.getSourceAmount(),
                transferHistory.getFee(),
                transferHistory.getUsdExchangeRate(),
                transferHistory.getUsdAmount(),
                transferHistory.getTargetCurrency().getCode(),
                transferHistory.getExchangeRate(),
                transferHistory.getTargetAmount(),
                transferHistory.getRequestedDate());
    }
}

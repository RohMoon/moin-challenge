package com.moinchallenge.dto.response;

import com.moinchallenge.entity.User;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class TransferListResponse {
    private int resultCode;
    private String resultMsg;
    private String userId;
    private String name;
    private long todayTransferCount;
    private double todayTransferUsdAmount;
    private List<TransferHistoryResponse> history;

    public static TransferListResponse of(String userId,
                                          String userName,
                                          long todayTransferCount,
                                          double todayTransferUsdAmount,
                                          List<TransferHistoryResponse> history) {
        return TransferListResponse.builder()
                .resultCode(200)
                .resultMsg("OK")
                .userId(userId)
                .name(userName)
                .todayTransferCount(todayTransferCount)
                .todayTransferUsdAmount(todayTransferUsdAmount)
                .history(history)
                .build();
    }
}

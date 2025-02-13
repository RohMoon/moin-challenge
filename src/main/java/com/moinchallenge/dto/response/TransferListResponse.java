package com.moinchallenge.dto.response;

import com.moinchallenge.entity.User;
import lombok.*;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class TransferListResponse {
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
                .userId(userId)
                .name(userName)
                .todayTransferCount(todayTransferCount)
                .todayTransferUsdAmount(todayTransferUsdAmount)
                .history(history)
                .build();
    }
}

package com.moinchallenge.dto.response;

import lombok.*;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class TransferListWrapperResponse extends BaseResponse {
    private String userId;
    private String name;
    private long todayTransferCount;
    private double todayTransferUsdAmount;
    private List<TransferHistoryResponse> history;

    protected TransferListWrapperResponse(int resultCode, String resultMsg, TransferListResponse transferListResponse) {
        super(resultCode, resultMsg);
        this.userId = transferListResponse.getUserId();
        this.name = transferListResponse.getName();
        this.todayTransferCount = transferListResponse.getTodayTransferCount();
        this.todayTransferUsdAmount = transferListResponse.getTodayTransferUsdAmount();
        this.history = transferListResponse.getHistory();
    }

    public static TransferListWrapperResponse of(int resultCode, String resultMsg, TransferListResponse transferListResponse) {
        return new TransferListWrapperResponse(resultCode, resultMsg, transferListResponse);
    }
}

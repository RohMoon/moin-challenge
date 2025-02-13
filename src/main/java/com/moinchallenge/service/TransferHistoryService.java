package com.moinchallenge.service;

import com.moinchallenge.dto.response.TransferHistoryResponse;
import com.moinchallenge.dto.response.TransferListResponse;
import com.moinchallenge.entity.TransferHistory;
import com.moinchallenge.entity.Quote;
import com.moinchallenge.entity.User;
import com.moinchallenge.repository.TransferHistoryRepository;
import com.moinchallenge.repository.UserRepository;
import com.moinchallenge.utils.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransferHistoryService {
    private final TransferHistoryRepository transferHistoryRepository;
    private final UserRepository userRepository;

    public double calculateDailySum(Long userPk, Quote quote, LocalDateTime now) {
        double dailySum = transferHistoryRepository.sumAmountByIdAndTransferDateTime(userPk, now);
        return dailySum + quote.getTargetAmount();
    }

    public void saveTransferHistory(User user, Quote quote, LocalDateTime now) {
        TransferHistory transferHistory = TransferHistory.createFromQuote(user.getId(), quote, now);
        transferHistoryRepository.save(transferHistory);
    }

    public TransferListResponse getTransferHistory() {
        String currentUserId = SecurityUtil.getCurrentUserId();
        User user = userRepository.findByUserId(currentUserId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
        Long userPk = user.getId();

        List<TransferHistory> historyList = transferHistoryRepository.findByUserPkOrderByRequestedDateDesc(userPk);
        List<TransferHistoryResponse> historyDtoList = historyList.stream()
                .map(TransferHistoryResponse::from)
                .toList();

        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
        long todayTransferCount = transferHistoryRepository.countByUserPkAndRequestedDate(userPk, now);
        double todayTransferUsdAmount = transferHistoryRepository.sumUsdAmountByUserPkAndRequestedDate(userPk, now);

        return  TransferListResponse.of(
                        currentUserId,
                        user.getName(),
                        todayTransferCount,
                        todayTransferUsdAmount,
                        historyDtoList
                );
    }

}

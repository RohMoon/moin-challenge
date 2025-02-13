package com.moinchallenge.service;

import com.moinchallenge.entity.Quote;
import com.moinchallenge.entity.User;
import com.moinchallenge.repository.QuoteRepository;
import com.moinchallenge.repository.UserRepository;
import com.moinchallenge.utils.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
@RequiredArgsConstructor
public class TransferService {
    private final QuoteRepository quoteRepository;
    private final UserRepository userRepository;
    private final TransferHistoryService transferHistoryService;

    public void requestTransfer(Long quoteId) {
        Quote quote = quoteRepository.findById(quoteId)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 견적서입니다."));

        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Seoul"));

        if (quote.isExpired(now)) {
            throw new IllegalArgumentException("견적서가 만료 되었습니다.");
        }

        User user = userRepository.findByUserId(SecurityUtil.getCurrentUserId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        double newSum = transferHistoryService.calculateDailySum(user.getId(), quote, now);
        if (!user.canTransfer(newSum)) {
            throw new IllegalArgumentException("오늘 송금 한도를 초과하였습니다.");
        }

        transferHistoryService.saveTransferHistory(user, quote, now);
    }
}

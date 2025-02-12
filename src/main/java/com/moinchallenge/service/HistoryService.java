package com.moinchallenge.service;

import com.moinchallenge.entity.History;
import com.moinchallenge.entity.Quote;
import com.moinchallenge.entity.User;
import com.moinchallenge.repository.HistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class HistoryService {
    private final HistoryRepository historyRepository;

    public double calculateDailySum(Long userPk, Quote quote, LocalDateTime now) {
        double dailySum = historyRepository.sumAmountByIdAndTransferDateTime(userPk, now);
        return dailySum + quote.getTargetAmount();
    }

    public void saveTransferHistory(User user, Quote quote, LocalDateTime now) {
        History history = History.createFromQuote(user.getId(), quote, now);
        historyRepository.save(history);
    }

}

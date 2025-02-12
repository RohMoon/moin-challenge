package com.moinchallenge.service;

import com.moinchallenge.constant.Currency;
import com.moinchallenge.dto.request.QuoteRequest;
import com.moinchallenge.dto.response.QuoteResponse;
import com.moinchallenge.entity.Quote;
import com.moinchallenge.repository.QuoteRepository;
import com.moinchallenge.utils.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuoteService {
    private final ExchangeRateService exchangeRateService;
    private final QuoteRepository quoteRepository;

    public QuoteResponse calculateQuote(QuoteRequest req) {

        long amount = req.getAmount();
        Currency targetCurrency = Currency.fromCode(req.getTargetCurrency());

        String userId = SecurityUtil.getCurrentUserId();
        // 4) 환율 가져오기
        // e.g. targetCurrency= "JPY" => "FRX.KRWJPY"
        double exchangeRate = exchangeRateService.getExchangeRate(targetCurrency);
        Quote quote = Quote.create(amount, targetCurrency, exchangeRate,userId);
        Quote savedQuote = quoteRepository.save(quote);

//        String expireTime = expireAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        return QuoteResponse.from(savedQuote);
    }
}

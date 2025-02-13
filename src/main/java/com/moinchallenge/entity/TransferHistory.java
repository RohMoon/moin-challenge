package com.moinchallenge.entity;

import com.moinchallenge.constant.Currency;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name="transferHistory")
public class TransferHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userPk;
    private Long sourceAmount;
    private Long fee;
    private Double usdExchangeRate;
    private Double usdAmount;

    @Enumerated(EnumType.STRING)
    private Currency targetCurrency;
    private Double exchangeRate;
    private Double targetAmount;
    private LocalDateTime requestedDate;

    public static TransferHistory createFromQuote(Long userPk, Quote quote, LocalDateTime now){
        return TransferHistory.builder()
                .userPk(userPk)
                .sourceAmount(quote.getSourceAmount())
                .fee(quote.getFee())
                .usdExchangeRate(quote.getExchangeRate())
                .usdAmount(quote.getTargetAmount())
                .targetCurrency(quote.getCurrency())
                .exchangeRate(quote.getExchangeRate())
                .targetAmount(quote.getTargetAmount())
                .requestedDate(now)
                .build();
    }
}

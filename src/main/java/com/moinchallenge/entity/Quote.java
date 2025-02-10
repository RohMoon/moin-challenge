package com.moinchallenge.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Quote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    private Double exchangeRate;
    private LocalDateTime expireTime;
    private Double targetAmount;

    public static Quote of(
            Long quoteId,
            Double exchangeRate,
            LocalDateTime expireTime,
            Double targetAmount
    ) {
        return new Quote(quoteId, exchangeRate, expireTime, targetAmount);
    }
}

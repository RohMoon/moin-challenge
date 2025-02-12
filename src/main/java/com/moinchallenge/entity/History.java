package com.moinchallenge.entity;

import com.moinchallenge.constant.Currency;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Entity
@Builder
@AllArgsConstructor
@Table(name="history")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class History {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userPk;
    private Long sourceAmount;
    private Long fee;
    private Long usdExchangeRate;
    private Double usdAmount;
    @Enumerated(EnumType.STRING)
    private Currency targetCurrency;
    private Double exchangeRate;
    private Double targetAmount;
    private LocalDateTime requestedDate;
}

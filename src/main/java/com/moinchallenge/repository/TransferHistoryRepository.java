package com.moinchallenge.repository;

import com.moinchallenge.entity.History;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;

public interface TransferHistoryRepository extends JpaRepository<History, Long> {
    @Query("SELECT COALESCE(SUM(th.transferAmount), 0) FROM History th " +
            "WHERE th.userId = :userId AND th.requestedDate = :dateTime")
    double sumAmountByIdAndTransferDateTime(Long id, LocalDateTime dateTime);
}

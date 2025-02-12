package com.moinchallenge.repository;

import com.moinchallenge.entity.History;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;

public interface HistoryRepository extends JpaRepository<History, Long> {
    @Query("SELECT COALESCE(SUM(th.targetAmount), 0) FROM History th " +
            "WHERE th.userPk = :userPk " +
            "AND FUNCTION('DATE', th.requestedDate) = FUNCTION('DATE', :dateTime)")
    double sumAmountByIdAndTransferDateTime(Long userPk, LocalDateTime dateTime);
}

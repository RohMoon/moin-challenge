package com.moinchallenge.repository;

import com.moinchallenge.entity.TransferHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransferHistoryRepository extends JpaRepository<TransferHistory, Long> {
    List<TransferHistory> findByUserPkOrderByRequestedDateDesc(Long userPk);

    @Query("SELECT COALESCE(SUM(th.targetAmount), 0) FROM TransferHistory th " +
            "WHERE th.userPk = :userPk " +
            "AND CAST(th.requestedDate AS date) = CAST(:today AS date)")
    double sumAmountByIdAndTransferDateTime(Long userPk, LocalDateTime today);

    @Query("SELECT COUNT(h) FROM TransferHistory h " +
            "WHERE h.userPk = :userPk " +
            "AND CAST(h.requestedDate AS date) = CAST(:today AS date)")
    long countByUserPkAndRequestedDate(Long userPk, LocalDateTime today);

    @Query("SELECT COALESCE(SUM(h.usdAmount), 0) FROM TransferHistory h " +
            "WHERE h.userPk = :userPk " +
            "AND CAST(h.requestedDate AS date) = CAST(:today AS date)")
    double sumUsdAmountByUserPkAndRequestedDate(Long userPk, LocalDateTime today);
}

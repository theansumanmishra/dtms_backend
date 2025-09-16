package com.disputetrackingsystem.repository;

import com.disputetrackingsystem.entity.SavingsAccountTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public interface SavingsAccountTransactionRepository extends JpaRepository <SavingsAccountTransaction, Long> {

    // all transactions for an account
    List<SavingsAccountTransaction> findBySavingsAccountId(Long savingsAccountId);

    // only recent (last 90 days)
    @Query("SELECT t FROM SavingsAccountTransaction t " +
            "WHERE t.savingsAccount.id = :savingsAccountId " +
            "AND t.transactionDate >= :startDate")
    List<SavingsAccountTransaction> findAllFromDate(
            @Param("savingsAccountId") Long savingsAccountId,
            @Param("startDate") LocalDateTime startDate);

    // get transaction by mode (ATM, POS)
    List<SavingsAccountTransaction> findBySavingsAccountIdAndTransactionModeAndIdNot
        (Long savingsAccountId, String transactionMode,Long id);
}

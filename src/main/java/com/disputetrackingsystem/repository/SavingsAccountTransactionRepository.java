package com.disputetrackingsystem.repository;

import com.disputetrackingsystem.entity.SavingsAccountTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SavingsAccountTransactionRepository extends JpaRepository <SavingsAccountTransaction, Long> {

    @Query("from SavingsAccountTransaction where savingsAccount.id=:savingsAccountId ")
    List<SavingsAccountTransaction> findBySavingsAccountId(@Param("savingsAccountId") Long savingsAccountId);
}

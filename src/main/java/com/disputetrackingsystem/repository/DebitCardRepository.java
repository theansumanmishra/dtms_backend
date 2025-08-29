package com.disputetrackingsystem.repository;

import com.disputetrackingsystem.entity.DebitCard;
import com.disputetrackingsystem.entity.SavingsAccountTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DebitCardRepository extends JpaRepository <DebitCard, Long> {
    @Query("from DebitCard where savingsAccount.id=:savingsAccountId ")
    List<DebitCard> findBySavingsAccountId(@Param("savingsAccountId") Long savingsAccountId);
}

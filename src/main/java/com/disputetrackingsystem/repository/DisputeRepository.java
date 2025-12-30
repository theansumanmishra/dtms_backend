package com.disputetrackingsystem.repository;

import com.disputetrackingsystem.model.Dispute;
import com.disputetrackingsystem.model.SavingsAccountTransaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface DisputeRepository extends JpaRepository<Dispute, Long> {

    // SHOW FILTERED DISPUTES(ALL/UNDER-VERIFICATION/UNDER-REVIEWED)
    Page<Dispute> findByReviewedByIsNull(Pageable pageable);

    Page<Dispute> findBySubStatus_NameIgnoreCase(String name, Pageable pageable);

    // Count Disputes by Sub-Status
    Long countBySubStatus_NameIgnoreCase(String subStatusName);

    // Find Dispute by Day,Week,Month & Year
    long countByCreatedDateBetween(Date start, Date end);

    long countByCreatedDateAfter(Date date);

    // Find Dispute by Account Number
    Page<Dispute> findBySavingsAccountTransaction_SavingsAccount_AccountNumber(
            long accountNumber,
            Pageable pageable);

    // Get Dispute Raise Stat for individual user
    @Query("""
                SELECT
                    SUM(CASE WHEN d.createdBy.id = :userId THEN 1 ELSE 0 END),
                    SUM(CASE WHEN d.reviewedBy.id = :userId THEN 1 ELSE 0 END)
                FROM Dispute d
            """)
    Object getDisputeStatsForUser(@Param("userId") Long userId);

    // Get top 10 recent disputes by createdDate
    List<Dispute> findTop10ByOrderByCreatedDateDesc();

    // Check if dispute already exists for this transaction
    boolean existsBySavingsAccountTransaction(SavingsAccountTransaction savingsAccountTransaction);
}

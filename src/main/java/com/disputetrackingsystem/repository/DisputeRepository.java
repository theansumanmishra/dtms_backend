package com.disputetrackingsystem.repository;

import com.disputetrackingsystem.model.Dispute;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public interface DisputeRepository extends JpaRepository <Dispute, Long> {
    Page<Dispute> findByReviewedByIsNull(Pageable pageable);

    Long countBySubStatus_NameIgnoreCase(String subStatusName);

    long countByCreatedDateBetween(Date start, Date end);
    long countByCreatedDateAfter(Date date);


    Page<Dispute> findBySavingsAccountTransaction_SavingsAccount_AccountNumber(
            long accountNumber,
            Pageable pageable
    );
}

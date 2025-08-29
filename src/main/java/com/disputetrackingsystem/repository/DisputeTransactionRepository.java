package com.disputetrackingsystem.repository;

import com.disputetrackingsystem.entity.DisputeTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DisputeTransactionRepository extends JpaRepository<DisputeTransaction, Long> {

}

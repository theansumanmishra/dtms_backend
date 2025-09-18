package com.disputetrackingsystem.repository;

import com.disputetrackingsystem.model.DisputeTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DisputeTransactionRepository extends JpaRepository<DisputeTransaction, Long> {

}

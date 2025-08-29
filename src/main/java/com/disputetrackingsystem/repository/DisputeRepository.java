package com.disputetrackingsystem.repository;

import com.disputetrackingsystem.entity.Dispute;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DisputeRepository extends JpaRepository <Dispute, Long> {

}

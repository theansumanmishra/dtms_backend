package com.disputetrackingsystem.repository;

import com.disputetrackingsystem.entity.Reason;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReasonRepository extends JpaRepository<Reason, Long> {
}

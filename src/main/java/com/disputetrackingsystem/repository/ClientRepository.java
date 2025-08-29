package com.disputetrackingsystem.repository;

import com.disputetrackingsystem.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository <Client, Long>{

}

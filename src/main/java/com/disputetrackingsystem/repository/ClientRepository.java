package com.disputetrackingsystem.repository;

import com.disputetrackingsystem.entity.Client;
import com.disputetrackingsystem.security.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClientRepository extends JpaRepository <Client, Long>{
    Optional<Client> findClientByName(String name);
    Optional<Client> findClientByPhone(String phone);
}
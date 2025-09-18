package com.disputetrackingsystem.repository;

import com.disputetrackingsystem.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ClientRepository extends JpaRepository <Client, Long>{
    // Search by name, email, or phone (partial, case-insensitive match)
    List<Client> findByNameContainingIgnoreCaseOrEmailContainingIgnoreCaseOrPhoneContainingIgnoreCase(
            String name, String email, String phone);
}
package com.disputetrackingsystem.rbac.repository;

import com.disputetrackingsystem.rbac.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
}

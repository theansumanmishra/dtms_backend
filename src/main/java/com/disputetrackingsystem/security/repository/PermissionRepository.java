package com.disputetrackingsystem.security.repository;

import com.disputetrackingsystem.security.model.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long>{
}

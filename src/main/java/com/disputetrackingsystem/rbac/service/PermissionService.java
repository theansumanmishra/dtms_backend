package com.disputetrackingsystem.rbac.service;

import com.disputetrackingsystem.rbac.model.Permission;
import com.disputetrackingsystem.rbac.repository.PermissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PermissionService {

    @Autowired
    private PermissionRepository permissionRepository;

    public Permission createPermission(Permission permission) {
        return permissionRepository.save(permission);
    }
}

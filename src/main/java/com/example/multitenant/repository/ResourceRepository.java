package com.example.multitenant.repository;

import com.example.multitenant.model.Resource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ResourceRepository extends JpaRepository<Resource, Long>, JpaSpecificationExecutor<Resource> {
    long countByOwnerId(Long ownerId);
    long countByTenantId(Long tenantId);
}

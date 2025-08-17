package com.example.multitenant.repository;

import com.example.multitenant.model.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TenantRepository extends JpaRepository<Tenant, Long> {
    Optional<Tenant> findBySchemaName(String schemaName);
}

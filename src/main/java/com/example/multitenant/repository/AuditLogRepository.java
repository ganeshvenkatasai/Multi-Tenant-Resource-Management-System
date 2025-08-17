package com.example.multitenant.repository;

import com.example.multitenant.model.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long> { }

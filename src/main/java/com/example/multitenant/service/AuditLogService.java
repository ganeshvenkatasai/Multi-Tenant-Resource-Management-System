package com.example.multitenant.service;

import com.example.multitenant.model.AuditLog;
import com.example.multitenant.repository.AuditLogRepository;
import org.springframework.stereotype.Service;

@Service
public class AuditLogService {
    private final AuditLogRepository repo;

    public AuditLogService(AuditLogRepository repo) { this.repo = repo; }

    public void log(Long userId, String action) {
        AuditLog log = new AuditLog();
        log.setUserId(userId);
        log.setAction(action);
        repo.save(log);
    }
}

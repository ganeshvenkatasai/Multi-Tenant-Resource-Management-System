package com.example.multitenant.controller;

import com.example.multitenant.model.AuditLog;
import com.example.multitenant.repository.AuditLogRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/audit-logs")
public class AuditLogController {
    private final AuditLogRepository repo;

    public AuditLogController(AuditLogRepository repo) { this.repo = repo; }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<AuditLog>> list() { return ResponseEntity.ok(repo.findAll()); }
}

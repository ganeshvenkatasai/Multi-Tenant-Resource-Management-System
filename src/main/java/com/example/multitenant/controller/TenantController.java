package com.example.multitenant.controller;

import com.example.multitenant.dto.request.TenantRequest;
import com.example.multitenant.dto.response.TenantResponse;
import com.example.multitenant.model.Tenant;
import com.example.multitenant.service.TenantService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tenants")
public class TenantController {
    private final TenantService tenants;

    public TenantController(TenantService tenants) { this.tenants = tenants; }

    // Assuming only platform super-admin can call these; enforce outside (e.g., gateway) or add a SUPERADMIN role.
    @PostMapping
    public ResponseEntity<TenantResponse> create(@Valid @RequestBody TenantRequest req) {
        Tenant t = tenants.createTenant(req);
        return ResponseEntity.ok(new TenantResponse(t.getId(), t.getName(), t.getSchemaName()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        tenants.deleteTenant(id);
        return ResponseEntity.noContent().build();
    }
}

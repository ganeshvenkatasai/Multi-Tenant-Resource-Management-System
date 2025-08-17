package com.example.multitenant.service;

import com.example.multitenant.dto.request.TenantRequest;
import com.example.multitenant.model.Tenant;
import com.example.multitenant.repository.TenantRepository;
import jakarta.transaction.Transactional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class TenantService {
    private final TenantRepository tenants;
    private final JdbcTemplate jdbc;

    public TenantService(TenantRepository tenants, JdbcTemplate jdbc) {
        this.tenants = tenants;
        this.jdbc = jdbc;
    }

    @Transactional
    public Tenant createTenant(TenantRequest req) {
        tenants.findBySchemaName(req.schemaName()).ifPresent(t -> { throw new RuntimeException("Schema exists"); });
        jdbc.execute("CREATE SCHEMA IF NOT EXISTS " + req.schemaName());
        jdbc.execute("SET search_path TO " + req.schemaName() + ";" +
                "CREATE TABLE IF NOT EXISTS users (id bigserial primary key, tenant_id bigint, username varchar(255) unique not null, password varchar(255) not null, role varchar(20) not null, deleted boolean not null default false);" +
                "CREATE TABLE IF NOT EXISTS resources (id bigserial primary key, name varchar(255), description text, owner_id bigint, tenant_id bigint, deleted boolean not null default false);" +
                "CREATE TABLE IF NOT EXISTS audit_logs (id bigserial primary key, user_id bigint, action varchar(255), timestamp timestamptz default now());"
        );
        Tenant t = new Tenant();
        t.setName(req.name());
        t.setSchemaName(req.schemaName());
        return tenants.save(t);
    }

    @Transactional
    public void deleteTenant(Long id) {
        var t = tenants.findById(id).orElseThrow(() -> new RuntimeException("Tenant not found"));
        jdbc.execute("DROP SCHEMA IF EXISTS " + t.getSchemaName() + " CASCADE");
        tenants.deleteById(id);
    }
}

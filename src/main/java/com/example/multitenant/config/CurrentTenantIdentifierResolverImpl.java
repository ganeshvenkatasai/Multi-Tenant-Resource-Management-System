package com.example.multitenant.config;

import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.springframework.stereotype.Component;
import com.example.multitenant.util.TenantContext;

@Component
public class CurrentTenantIdentifierResolverImpl implements CurrentTenantIdentifierResolver {

    @Override
    public String resolveCurrentTenantIdentifier() {
        String tenant = TenantContext.getTenantId();
        return tenant != null ? tenant : "public";
    }

    @Override
    public boolean validateExistingCurrentSessions() {
        return true;
    }
}
package com.example.multitenant.config;

import org.hibernate.cfg.AvailableSettings;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class MultiTenantConfig {

    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer(
            MultiTenantConnectionProvider multiTenantConnectionProvider,
            CurrentTenantIdentifierResolver tenantIdentifierResolver) {
        
        return props -> {
            props.put(AvailableSettings.MULTI_TENANT_CONNECTION_PROVIDER, multiTenantConnectionProvider);
            props.put(AvailableSettings.MULTI_TENANT_IDENTIFIER_RESOLVER, tenantIdentifierResolver);
            props.put("hibernate.multiTenancy", "SCHEMA");
            props.put("hibernate.hikari.dataSource.currentSchema", "public");
        };
    }
}
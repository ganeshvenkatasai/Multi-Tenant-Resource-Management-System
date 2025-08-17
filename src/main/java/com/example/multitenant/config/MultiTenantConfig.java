package com.example.multitenant.config;

import org.hibernate.MultiTenancyStrategy;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class MultiTenantConfig {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private MultiTenantConnectionProviderImpl multiTenantConnectionProvider;

    @Autowired
    private TenantIdentifierResolver tenantIdentifierResolver;

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(EntityManagerFactoryBuilder builder) {
        Map<String, Object> props = new HashMap<>();
        props.put(org.hibernate.cfg.Environment.MULTI_TENANT, MultiTenancyStrategy.SCHEMA);
        props.put(org.hibernate.cfg.Environment.MULTI_TENANT_CONNECTION_PROVIDER, multiTenantConnectionProvider);
        props.put(org.hibernate.cfg.Environment.MULTI_TENANT_IDENTIFIER_RESOLVER, tenantIdentifierResolver);
        return builder
                .dataSource(dataSource)
                .packages("com.example.multitenant.model")
                .properties(props)
                .build();
    }

    @Bean
    public PlatformTransactionManager transactionManager(LocalContainerEntityManagerFactoryBean emf) {
        return new JpaTransactionManager(emf.getObject());
    }
}

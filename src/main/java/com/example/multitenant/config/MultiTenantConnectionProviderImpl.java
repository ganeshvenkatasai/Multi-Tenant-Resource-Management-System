package com.example.multitenant.config;

import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

@Component
public class MultiTenantConnectionProviderImpl implements MultiTenantConnectionProvider {

    @Autowired
    private DataSource dataSource;

    @Override
    public Connection getAnyConnection() throws SQLException {
        return DataSourceUtils.getConnection(dataSource);
    }

    @Override
    public void releaseAnyConnection(Connection connection) throws SQLException {
        DataSourceUtils.releaseConnection(connection, dataSource);
    }

    @Override
    public Connection getConnection(String tenantIdentifier) throws SQLException {
        final Connection connection = getAnyConnection();
        try (Statement st = connection.createStatement()) {
            st.execute("set search_path to " + tenantIdentifier);
        }
        return connection;
    }

    @Override
    public void releaseConnection(String tenantIdentifier, Connection connection) throws SQLException {
        try (Statement st = connection.createStatement()) {
            st.execute("set search_path to public");
        } finally {
            releaseAnyConnection(connection);
        }
    }

    @Override public boolean supportsAggressiveRelease() { return false; }
    @Override public boolean isUnwrappableAs(Class unwrapType) { return false; }
    @Override public <T> T unwrap(Class<T> unwrapType) { return null; }
}

package com.example.multitenant.config;

import org.hibernate.engine.jdbc.connections.spi.AbstractMultiTenantConnectionProvider;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Component
public class MultiTenantConnectionProviderImpl 
    extends AbstractMultiTenantConnectionProvider<String> {

    private final DataSource dataSource;

    public MultiTenantConnectionProviderImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    protected ConnectionProvider getAnyConnectionProvider() {
        return createConnectionProvider(dataSource);
    }

    @Override
    protected ConnectionProvider selectConnectionProvider(String tenantIdentifier) {
        return getAnyConnectionProvider();
    }

    @Override
    public Connection getConnection(String tenantIdentifier) throws SQLException {
        Connection connection = super.getConnection(tenantIdentifier);
        connection.createStatement().execute("SET SCHEMA '" + tenantIdentifier + "'");
        return connection;
    }

    private ConnectionProvider createConnectionProvider(DataSource dataSource) {
        return new ConnectionProvider() {
            @Override
            public Connection getConnection() throws SQLException {
                return dataSource.getConnection();
            }
            
            @Override
            public void closeConnection(Connection connection) throws SQLException {
                connection.close();
            }
            
            @Override
            public boolean supportsAggressiveRelease() {
                return false;
            }
            
            @Override
            public boolean isUnwrappableAs(Class<?> unwrapType) {
                return false;
            }
            
            @Override
            public <T> T unwrap(Class<T> unwrapType) {
                return null;
            }
        };
    }
}
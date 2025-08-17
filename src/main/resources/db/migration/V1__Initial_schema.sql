-- Public schema to manage tenants
CREATE TABLE IF NOT EXISTS tenants (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    schema_name VARCHAR(255) UNIQUE NOT NULL
);

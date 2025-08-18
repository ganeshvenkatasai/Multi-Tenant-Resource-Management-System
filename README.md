# Multi-Tenant Resource Management System

## Overview

This backend system provides multi-tenant resource management for organizations, implementing **tenant-level isolation**, secure authentication/authorization, precise quota enforcement, comprehensive audit logging, and advanced API features. Built using **Spring Boot** and **Hibernate**, the system is modular, scalable, and aligns with modern software engineering practices.

---

## System Design Decisions

### 1. **Multi-Tenancy Strategy**
- **Schema-per-Tenant:** Each tenant’s data is stored in a dedicated database schema, enforced by Hibernate’s multi-tenancy implementation. Tenant context (tenantId) is extracted from request headers for each API call and used to switch schemas dynamically.
- **Tenant Isolation at All Levels:** Controller, service, repository, and security layers verify tenant context on every operation, ensuring users/resources/audit logs cannot be viewed or altered across tenants.

### 2. **Authentication & Authorization**
- **JWT-Based:** Users login via `/auth/login` and receive JWT tokens encoding their role, userId, and tenantId. All endpoints require the tenantId in the headers and JWT in the `Authorization` header.
- **Role-Based Access Control:** Three roles per tenant—`ADMIN`, `MANAGER`, `EMPLOYEE`—with fine-grained API permissions enforced using Spring Method Security.

### 3. **Soft Deletes**
- **User & Resource Entities:** Soft deletes implemented via a boolean `deleted` or `active` flag; records are retained but excluded from API results, simplifying restoration and audit compliance.

### 4. **Audit Logging**
- **Every Write Action Logged:** All create, update, and delete actions across resources and users are captured in the AuditLog table, with timestamp, user, action, and affected entity.

### 5. **Performance Optimization**
- **Indexes:** OwnerId, tenantId, and frequently queried fields are indexed for rapid lookups.
- **Batch Operations:** Hibernate batch settings optimized for updates and inserts across tenants.
- **Caching:** Read-heavy endpoints (e.g., `GET /resources`) employ Redis caching.

### 6. **Quota Enforcement**
- **Static & Dynamic Quotas:** Default quotas (50 users, 500 resources/tenant, 10 resources/user) and optional per-tenant quotas configurable by admin users.

### 7. **Security**
- **Spring Security:** Stateless sessions, strong password hashing, CORS configuration, and protection against tenant privilege escalation.
- **Rate Limiting:** API rate limits applied per tenant (bonus).

### 8. **Modular Organization**
- **Packages:** Organized into configuration, controller, DTO, exception, filter, model, repository, service, and utility layers for separation of concerns.

---

## Instructions to Run the Project

### Prerequisites

- **Java 17+**
- **PostgreSQL**
- **Docker & Docker Compose** (optional)
- **Redis** (for caching) (optional)

### Environment Setup

1. **Clone the Repository**
   ```sh
   git clone <repo-url>
   cd multitenant-resource-management
   ```

2. **Database Setup**
   - Create a main 'public' schema in PostgreSQL.
   - The system creates new schemas per tenant automatically.

3. **Configure Environment**
   - Review and update database settings in `src/main/resources/application.yml` for local/dev/prod environments.
   - For Redis caching, ensure Redis is running and update settings if needed.

4. **Run with Docker**
   ```sh
   docker-compose up
   ```
   *or build manually:*
   ```sh
   mvn clean install
   java -jar target/multitenant-resource-management-0.0.1-SNAPSHOT.jar
   ```

5. **Kubernetes Deployment** (optional)
   - See `kubernetes/` folder for deployment, service, ingress, and configmap YAML files.

6. **Health Check**
   ```sh
   GET http://localhost:8080/actuator/health
   ```

---

## API Documentation

### TENANT MANAGEMENT (Super Admin Only)
| Endpoint                           | Method | Description                                | Required Role      |
|-------------------------------------|--------|--------------------------------------------|--------------------|
| `/tenants`                         | POST   | Create a new tenant                        | Super Admin        |
| `/tenants/{id}`                    | DELETE | Delete a tenant (cascades all data)        | Super Admin        |

**Request Header:** `tenantId: <target-tenant-id>`  
**Body Example (POST):**
```json
{
  "name": "Acme Corp",
  "schemaName": "acme"
}
```

---

### AUTHENTICATION
| Endpoint           | Method | Description           | Required Role    |
|--------------------|--------|-----------------------|------------------|
| `/auth/login`      | POST   | Login, obtain JWT     | Any              |

**Body Example:**  
```json
{
  "username": "adminuser",
  "password": "secret",
  "tenantId": "acme"
}
```
**Returns:** JWT token with tenant-specific claims.

---

### USER MANAGEMENT (Admin Only)
| Endpoint          | Method   | Description                       | Required Role |
|-------------------|----------|-----------------------------------|--------------|
| `/users`          | POST     | Create user within tenant         | Admin        |
| `/users/{id}`     | DELETE   | Soft delete user                  | Admin        |

**Body Example (POST):**  
```json
{
  "username": "user2",
  "password": "pass",
  "role": "MANAGER"
}
```

---

### RESOURCE MANAGEMENT
| Endpoint                   | Method | Description                   | Role             |
|----------------------------|--------|-------------------------------|------------------|
| `/resources`               | POST   | Create resource               | Admin/Manager    |
| `/resources/{id}`          | PUT    | Update resource               | Admin/Manager    |
| `/resources/{id}`          | DELETE | Soft delete resource          | Admin/Manager    |
| `/resources`               | GET    | List resources in tenant      | Employee         |
| `/resources/{id}`          | GET    | View resource details         | Employee         |
| `/resources/search`        | GET    | Search/filter resources       | Employee         |

**Body Example (POST):**  
```json
{
  "name": "ProjectPlan",
  "description": "Details...",
  "ownerId": "3"
}
```
**Query Params (search):**  
`name=...&ownerId=...&page=0&size=20`

---

### AUDIT LOGS (Admin Only)
| Endpoint           | Method | Description                         | Role    |
|--------------------|--------|-------------------------------------|---------|
| `/audit-logs`      | GET    | List audit logs for tenant actions  | Admin   |

---

## Advanced Features

### 1. **Rate Limiting**
Configurable per tenant. API returns `429` if exceeded.

### 2. **Caching**
Frequently accessed resources (GET/list/search) are cached using Redis.

### 3. **Dynamic Quotas**
Admins can update per-tenant limits via dedicated API (not in MVP).

### 4. **Testing**
Unit and integration tests for multi-tenant services are in `src/test/`.

---

## Entity Relationships

- **Tenant** (1..N) → **User**, **Resource**, **AuditLog**
- **User** (1..N) → **Resource**
- **AuditLog** references actions by users on resources.

---

## Security Best-Practices

- JWT signature secret rotated regularly.
- Bcrypt for password storage.
- Strict tenant context validation in every controller/service/repository.
- All API access controlled by role and tenant.
- CORS settings for cross-domain frontend integration (`WebConfig.java`).
- Health endpoint for infrastructure monitoring.

---

## Database Schema Approach

- Uses PostgreSQL schemas; each tenant (organization) has an isolated schema upon creation.
- All ORM models (`User`, `Resource`, `AuditLog`, etc.) are mapped per schema.
- Spring/Hibernate multi-tenancy ensures correct schema based on tenantId.

---

## Contribution & Maintenance

- PRs welcome for improving test coverage, new features (rate limiting, quota config), or performance.
- See CHANGELOG.md for updates.

---

## Contacts / Support

- Issues: Submit via GitHub Issues.
- For urgent support, contact maintainer email in project settings.

---

## Folder Structure

- `/src/main/java/com/example/multitenant/` – Main codebase (config, controllers, models, services)
- `/src/main/resources/` – Config files, migrations, templates
- `/kubernetes/` – Container orchestration templates
- `/test/` – Unit and integration tests
- `/target/` – Build output

---

## Additional Notes

- See `application.yml` for configuration, including default quota and security settings.
- Docker & K8s support provided for production deployments.
- Extendable for custom quotas, audits, and tenant types.

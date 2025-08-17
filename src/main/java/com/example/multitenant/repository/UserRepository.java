package com.example.multitenant.repository;

import com.example.multitenant.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    long countByTenantId(Long tenantId);
    long countByOwnerId(Long ownerId);
}

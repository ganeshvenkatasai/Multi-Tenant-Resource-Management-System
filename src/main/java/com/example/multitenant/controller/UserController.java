package com.example.multitenant.controller;

import com.example.multitenant.dto.request.UserRequest;
import com.example.multitenant.dto.response.UserResponse;
import com.example.multitenant.model.User;
import com.example.multitenant.service.UserService;
import com.example.multitenant.util.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService users;
    private final JwtUtil jwt;

    public UserController(UserService users, JwtUtil jwt) { this.users = users; this.jwt = jwt; }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponse> create(@RequestHeader("Authorization") String auth, @Valid @RequestBody UserRequest req) {
        String token = auth.substring(7);
        String tenantSchema = jwt.getTenant(token);
        // In real systems, map schema->tenantId; here we accept a numeric tenantId in schema like t_123
        Long tenantId = 1L; // placeholder; replace by lookup in public.tenants
        return ResponseEntity.ok(users.createUser(tenantId, req));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        users.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public ResponseEntity<List<User>> list() { return ResponseEntity.ok(users.list()); }
}

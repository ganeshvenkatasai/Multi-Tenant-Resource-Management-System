package com.example.multitenant.service;

import com.example.multitenant.dto.request.UserRequest;
import com.example.multitenant.dto.response.UserResponse;
import com.example.multitenant.model.User;
import com.example.multitenant.repository.ResourceRepository;
import com.example.multitenant.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository users;
    private final PasswordEncoder encoder;

    public UserService(UserRepository users, PasswordEncoder encoder) {
        this.users = users;
        this.encoder = encoder;
    }

    public UserResponse createUser(Long tenantId, UserRequest req) {
        if (users.countByTenantId(tenantId) >= 50) throw new RuntimeException("Tenant user quota exceeded");
        User u = new User();
        u.setUsername(req.username());
        u.setPassword(encoder.encode(req.password()));
        u.setRole(req.role());
        u.setTenantId(tenantId);
        u = users.save(u);
        return new UserResponse(u.getId(), u.getUsername(), u.getRole());
    }

    public void deleteUser(Long id) {
        users.deleteById(id);
    }

    public List<User> list() { return users.findAll(); }
}

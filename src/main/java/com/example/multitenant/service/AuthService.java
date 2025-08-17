package com.example.multitenant.service;

import com.example.multitenant.dto.request.AuthRequest;
import com.example.multitenant.dto.response.AuthResponse;
import com.example.multitenant.model.User;
import com.example.multitenant.repository.UserRepository;
import com.example.multitenant.util.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository users;
    private final PasswordEncoder encoder;
    private final JwtUtil jwt;

    public AuthService(UserRepository users, PasswordEncoder encoder, JwtUtil jwt) {
        this.users = users;
        this.encoder = encoder;
        this.jwt = jwt;
    }

    public AuthResponse login(AuthRequest req) {
        var user = users.findByUsername(req.username())
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));
        if (!encoder.matches(req.password(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }
        String token = jwt.generateToken(user.getUsername(), user.getRole().name(), req.tenantId());
        return new AuthResponse(token);
    }
}

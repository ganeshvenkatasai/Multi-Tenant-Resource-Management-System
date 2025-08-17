package com.example.multitenant.dto.response;

import com.example.multitenant.model.User;

public record UserResponse(Long id, String username, User.Role role) {}

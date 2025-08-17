package com.example.multitenant.dto.request;

import jakarta.validation.constraints.NotBlank;

public record AuthRequest(@NotBlank String username, @NotBlank String password, @NotBlank String tenantId) {}

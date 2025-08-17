package com.example.multitenant.dto.request;

import jakarta.validation.constraints.NotBlank;

public record TenantRequest(@NotBlank String name, @NotBlank String schemaName) {}

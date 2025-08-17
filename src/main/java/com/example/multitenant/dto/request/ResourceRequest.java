package com.example.multitenant.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ResourceRequest(@NotBlank String name, String description, @NotNull Long ownerId) {}

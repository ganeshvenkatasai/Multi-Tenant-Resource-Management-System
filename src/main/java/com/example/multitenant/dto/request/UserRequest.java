package com.example.multitenant.dto.request;

import com.example.multitenant.model.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserRequest(@NotBlank String username, @NotBlank String password, @NotNull User.Role role) {}

package com.example.multitenant.dto.response;

public record ResourceResponse(Long id, String name, String description, Long ownerId) {}

package com.example.multitenant.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Entity @Table(name = "tenants", schema = "public")
@Getter @Setter
public class Tenant {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    @Column(unique = true, nullable = false)
    private String schemaName; // e.g. t_acme
}

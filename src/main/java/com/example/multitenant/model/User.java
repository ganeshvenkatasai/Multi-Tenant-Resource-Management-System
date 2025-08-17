package com.example.multitenant.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity @Table(name = "users")
@Getter @Setter
@SQLDelete(sql = "UPDATE users SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false")
public class User {
    public enum Role { ADMIN, MANAGER, EMPLOYEE }

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long tenantId; // reference to public.tenants.id (for safety checks)

    @Column(nullable = false, unique = true)
    private String username;

    @NotBlank
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(nullable = false)
    private boolean deleted = false;
}

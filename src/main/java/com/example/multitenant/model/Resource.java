package com.example.multitenant.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity @Table(name = "resources")
@Getter @Setter
@SQLDelete(sql = "UPDATE resources SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false")
public class Resource {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;

    private Long ownerId; // User.id
    private Long tenantId;

    @Column(nullable = false)
    private boolean deleted = false;
}

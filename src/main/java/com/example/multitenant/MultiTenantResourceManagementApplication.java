package com.example.multitenant;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class MultiTenantResourceManagementApplication {
    public static void main(String[] args) {
        SpringApplication.run(MultiTenantResourceManagementApplication.class, args);
    }
}

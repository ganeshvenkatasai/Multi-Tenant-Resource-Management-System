package com.example.multitenant.controller;

import com.example.multitenant.dto.request.ResourceRequest;
import com.example.multitenant.dto.response.ResourceResponse;
import com.example.multitenant.model.Resource;
import com.example.multitenant.service.ResourceService;
import com.example.multitenant.util.JwtUtil;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/resources")
public class ResourceController {
    private final ResourceService resources;
    private final JwtUtil jwt;

    public ResourceController(ResourceService resources, JwtUtil jwt) { this.resources = resources; this.jwt = jwt; }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public ResponseEntity<ResourceResponse> create(@RequestHeader("Authorization") String auth, @RequestBody ResourceRequest req) {
        Long tenantId = 1L; // placeholder mapping
        return ResponseEntity.ok(resources.create(tenantId, req));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public ResponseEntity<ResourceResponse> update(@PathVariable Long id, @RequestBody ResourceRequest req) {
        return ResponseEntity.ok(resources.update(id, req));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        resources.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','EMPLOYEE')")
    public ResponseEntity<Page<Resource>> search(@RequestParam(required = false) String name,
                                                 @RequestParam(required = false) Long ownerId,
                                                 @RequestParam(defaultValue = "0") int page,
                                                 @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(resources.search(name, ownerId, page, size));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','EMPLOYEE')")
    public ResponseEntity<Resource> get(@PathVariable Long id) {
        return ResponseEntity.of(resources.search(null, null, 0, 1).stream()
                .filter(r -> r.getId().equals(id)).findFirst());
    }
}

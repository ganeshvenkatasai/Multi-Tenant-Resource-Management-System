package com.example.multitenant.service;

import com.example.multitenant.dto.request.ResourceRequest;
import com.example.multitenant.dto.response.ResourceResponse;
import com.example.multitenant.model.Resource;
import com.example.multitenant.repository.ResourceRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class ResourceService {
    private final ResourceRepository repo;

    public ResourceService(ResourceRepository repo) { this.repo = repo; }

    public ResourceResponse create(Long tenantId, ResourceRequest req) {
        if (repo.countByTenantId(tenantId) >= 500) throw new RuntimeException("Tenant resource quota exceeded");
        if (repo.countByOwnerId(req.ownerId()) >= 10) throw new RuntimeException("Owner resource quota exceeded");
        Resource r = new Resource();
        r.setName(req.name());
        r.setDescription(req.description());
        r.setOwnerId(req.ownerId());
        r.setTenantId(tenantId);
        r = repo.save(r);
        return new ResourceResponse(r.getId(), r.getName(), r.getDescription(), r.getOwnerId());
    }

    public ResourceResponse update(Long id, ResourceRequest req) {
        Resource r = repo.findById(id).orElseThrow(() -> new RuntimeException("Not found"));
        r.setName(req.name());
        r.setDescription(req.description());
        r.setOwnerId(req.ownerId());
        r = repo.save(r);
        return new ResourceResponse(r.getId(), r.getName(), r.getDescription(), r.getOwnerId());
    }

    public void delete(Long id) { repo.deleteById(id); }

    public Page<Resource> search(String name, Long ownerId, int page, int size) {
        Specification<Resource> spec = Specification.where(null);
        if (name != null && !name.isBlank()) {
            spec = spec.and((root, q, cb) -> cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
        }
        if (ownerId != null) {
            spec = spec.and((root, q, cb) -> cb.equal(root.get("ownerId"), ownerId));
        }
        return repo.findAll(spec, PageRequest.of(page, size));
    }
}

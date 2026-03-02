package com.schoolapp.controller;

import org.springframework.web.bind.annotation.*;

import com.schoolapp.dao.ProductionEntryRequest;
import com.schoolapp.dao.ProductionImportRequest;
import com.schoolapp.dao.ProductionImportResponse;
import com.schoolapp.entity.ProductionEntry;
import com.schoolapp.service.ProductionEntryService;

import java.util.List;

@RestController
@RequestMapping("/api/production")

public class ProductionEntryController {

    private final ProductionEntryService service;

    public ProductionEntryController(ProductionEntryService service) {
        this.service = service;
    }

    @PostMapping
    public ProductionEntry create(@RequestBody ProductionEntryRequest request) {
        return service.save(request);
    }

    @PutMapping("/{id}")
    public ProductionEntry update(
            @PathVariable Long id,
            @RequestBody ProductionEntryRequest request) {
        return service.update(id, request);
    }

    @GetMapping
    public List<ProductionEntry> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public ProductionEntry getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    @PutMapping("/{id}/approve")
    public ProductionEntry approve(
            @PathVariable Long id,
            @RequestParam Long userId,
            @RequestParam String role) {
        return service.approve(id, userId, role);
    }

    @PostMapping("/{id}/reject")
    public ProductionEntry reject(
            @PathVariable Long id,
            @RequestParam Long userId,
            @RequestParam String role,
            @RequestParam(required = false) String reason) {
        return service.reject(id, userId, role, reason);
    }

    @PostMapping("/import")
    public ProductionImportResponse importProduction(
            @RequestBody ProductionImportRequest request) {
        return service.importProduction(request);
    }

}
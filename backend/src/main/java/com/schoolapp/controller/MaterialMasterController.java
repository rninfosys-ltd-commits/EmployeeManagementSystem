package com.schoolapp.controller;

import com.schoolapp.entity.MaterialMaster;
import com.schoolapp.service.MaterialMasterService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/material-master")
public class MaterialMasterController {

    private final MaterialMasterService service;

    public MaterialMasterController(MaterialMasterService service) {
        this.service = service;
    }

    @GetMapping
    public List<MaterialMaster> getAll() {
        return service.getAllActive();
    }

    @GetMapping("/org/{orgId}")
    public List<MaterialMaster> getByOrg(@PathVariable int orgId) {
        return service.getAllActiveByOrg(orgId);
    }

    @PostMapping
    public MaterialMaster create(@RequestBody MaterialMaster material) {
        return service.save(material);
    }

    @PutMapping("/{id}")
    public MaterialMaster update(
            @PathVariable Long id,
            @RequestBody MaterialMaster material) {
        return service.update(id, material);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}

package com.schoolapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.schoolapp.dao.RejectionDataDTO;
import com.schoolapp.service.RejectionDataService;

@RestController
@RequestMapping("/api/rejection")

public class RejectionDataController {

    @Autowired
    private RejectionDataService service;

    // CREATE
    @PostMapping
    public Object create(@RequestBody RejectionDataDTO dto) {
        return service.create(dto);
    }

    // GET ALL
    @GetMapping
    public Object getAll() {
        return service.getAll();
    }

    // GET BY ID
    @GetMapping("/{id}")
    public Object getById(@PathVariable Long id) {
        return service.getById(id);
    }

    // UPDATE
    @PutMapping("/{id}")
    public Object update(@PathVariable Long id,
            @RequestBody RejectionDataDTO dto) {
        return service.update(id, dto);
    }

    // DELETE (soft)
    @DeleteMapping("/{id}")
    public Object delete(@PathVariable Long id) {
        return service.delete(id);
    }
}

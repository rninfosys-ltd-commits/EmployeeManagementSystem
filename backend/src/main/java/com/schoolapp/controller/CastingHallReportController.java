package com.schoolapp.controller;

import org.springframework.web.bind.annotation.*;

import com.schoolapp.dao.CastingHallReportRequestDto;
import com.schoolapp.dao.CastingImportRequestDto;
import com.schoolapp.dao.CastingImportResponse;
import com.schoolapp.dao.RejectRequest;
import com.schoolapp.entity.CastingHallReport;
import com.schoolapp.service.CastingHallReportService;

import java.util.List;

@RestController
@RequestMapping("/api/casting-report")

public class CastingHallReportController {

    private final CastingHallReportService service;

    public CastingHallReportController(CastingHallReportService service) {
        this.service = service;
    }

    @PostMapping
    public CastingHallReport save(@RequestBody CastingHallReportRequestDto dto) {
        return service.save(dto);
    }

    @PutMapping("/{id}")
    public CastingHallReport update(@PathVariable Long id,
            @RequestBody CastingHallReportRequestDto dto) {
        return service.update(id, dto);
    }

    @GetMapping
    public List<CastingHallReport> getAll() {
        return service.getAll();
    }

    @PutMapping("/{id}/approve")
    public CastingHallReport approve(@PathVariable Long id,
            @RequestParam Long userId,
            @RequestParam String role) {

        return service.approve(id, userId, role);
    }

    @PostMapping("/reject/{id}")
    public CastingHallReport reject(@PathVariable Long id,
            @RequestParam Long userId,
            @RequestParam String role,
            @RequestBody RejectRequest req) {

        return service.reject(id, userId, role, req.getReason());
    }

    @GetMapping("/{id}")
    public CastingHallReport getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @PostMapping("/import")
    public CastingImportResponse importCasting(
            @RequestBody CastingImportRequestDto dto) {
        return service.importCasting(dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

}

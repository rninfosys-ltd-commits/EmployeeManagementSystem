package com.schoolapp.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import com.schoolapp.dao.CastingHallReportRequestDto;
import com.schoolapp.dao.CastingImportRequestDto;
import com.schoolapp.dao.CastingImportResponse;
import com.schoolapp.dao.RejectRequest;
import com.schoolapp.entity.CastingHallReport;
import com.schoolapp.service.CastingHallReportService;


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
    public Page<CastingHallReport> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String direction,
            @RequestParam(required = false) String plantName) {

        Sort sort = direction.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        return service.getAll(pageable, plantName);
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

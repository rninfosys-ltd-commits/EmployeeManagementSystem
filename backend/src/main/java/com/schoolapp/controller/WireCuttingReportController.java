package com.schoolapp.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.schoolapp.dao.RejectRequest;
import com.schoolapp.dao.WireCuttingImportRequestDto;
import com.schoolapp.dao.WireCuttingImportResponseDto;
import com.schoolapp.dao.WireCuttingReportRequestDto;
import com.schoolapp.entity.WireCuttingReport;
import com.schoolapp.service.WireCuttingReportService;

@RestController
@RequestMapping("/api/wire-cutting")

public class WireCuttingReportController {

    private final WireCuttingReportService service;

    public WireCuttingReportController(WireCuttingReportService service) {
        this.service = service;
    }

    @PostMapping
    public WireCuttingReport save(@RequestBody WireCuttingReportRequestDto dto) {
        return service.save(dto);
    }

    @PutMapping("/{id}")
    public WireCuttingReport update(
            @PathVariable Long id,
            @RequestBody WireCuttingReportRequestDto dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    @GetMapping
    public List<WireCuttingReport> getAll() {
        return service.getAll();
    }

    @PutMapping("/{id}/approve")
    public WireCuttingReport approve(
            @PathVariable Long id,
            @RequestParam Long userId,
            @RequestParam String role,
            @RequestParam String username) {
        return service.approve(id, userId, role, username);
    }

    @PostMapping("/{id}/reject")
    public WireCuttingReport reject(
            @PathVariable Long id,
            @RequestBody RejectRequest r,
            @RequestParam Long userId,
            @RequestParam String role,
            @RequestParam String username) {
        return service.reject(id, userId, role, username, r.getReason());
    }

    @PostMapping("/import")
    public WireCuttingImportResponseDto importWireCutting(
            @RequestBody WireCuttingImportRequestDto dto) {

        return service.importWireCutting(dto);
    }

}

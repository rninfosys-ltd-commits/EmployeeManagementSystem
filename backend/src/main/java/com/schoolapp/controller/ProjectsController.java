package com.schoolapp.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.schoolapp.dao.InquiryProjectCreateDto;
import com.schoolapp.dao.ProjectImportRequest;
import com.schoolapp.dao.ProjectsDto;
import com.schoolapp.service.ProjectsService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/projects")
public class ProjectsController {

    private final ProjectsService service;

    public ProjectsController(ProjectsService service) {
        this.service = service;
    }

    // ================= CREATE =================
    @PostMapping
    public ResponseEntity<ProjectsDto> create(@RequestBody ProjectsDto dto) {
        return ResponseEntity.ok(service.create(dto));
    }

    // ================= UPDATE =================
    @PutMapping("/{id}")
    public ResponseEntity<ProjectsDto> update(
            @PathVariable int id,
            @RequestBody ProjectsDto dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    // ================= GET ALL =================
    @GetMapping
    public ResponseEntity<List<ProjectsDto>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @PostMapping("/import")
    public ResponseEntity<Map<String, Object>> importProjects(
            @RequestBody ProjectImportRequest request) {

        return ResponseEntity.ok(
                service.bulkCreate(request.getProjects()));
    }

    @PostMapping("/from-inquiry")
    public ResponseEntity<ProjectsDto> createProjectFromInquiry(
            @Valid @RequestBody InquiryProjectCreateDto dto) {

        return ResponseEntity.ok(
                service.createFromInquiry(dto));
    }

}

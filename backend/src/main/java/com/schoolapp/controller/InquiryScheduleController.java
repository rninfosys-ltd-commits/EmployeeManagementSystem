package com.schoolapp.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.schoolapp.dao.InquiryScheduleDto;
import com.schoolapp.service.InquiryScheduleService;

@RestController
@RequestMapping("/api/inquiry-schedule")

public class InquiryScheduleController {

    private final InquiryScheduleService service;

    public InquiryScheduleController(InquiryScheduleService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<InquiryScheduleDto> create(@RequestBody InquiryScheduleDto dto) {
        return ResponseEntity.ok(service.save(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<InquiryScheduleDto> update(
            @PathVariable Long id,
            @RequestBody InquiryScheduleDto dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @GetMapping
    public ResponseEntity<List<InquiryScheduleDto>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<InquiryScheduleDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok("Inquiry Schedule Deleted");
    }
}
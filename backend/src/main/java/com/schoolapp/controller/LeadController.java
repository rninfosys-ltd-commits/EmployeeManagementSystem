package com.schoolapp.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.schoolapp.dao.InquiryLeadCreateDto;
import com.schoolapp.dao.LeadDto;
import com.schoolapp.service.LeadService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/leads")
//
public class LeadController {

    private final LeadService leadService;

    public LeadController(LeadService leadService) {
        this.leadService = leadService;
    }

    // ================= CREATE LEAD =================
    @PostMapping
    public ResponseEntity<LeadDto> createLead(@RequestBody LeadDto dto) {
        LeadDto saved = leadService.createLead(dto);
        return ResponseEntity.ok(saved);
    }

    // ================= GET ALL LEADS =================
    @GetMapping
    public ResponseEntity<List<LeadDto>> getAllLeads() {
        return ResponseEntity.ok(leadService.getAllLeads());
    }

    // ================= GET LEAD BY ID =================
    @GetMapping("/{id}")
    public ResponseEntity<LeadDto> getLeadById(@PathVariable int id) {
        return ResponseEntity.ok(leadService.getLeadById(id));
    }

    // ================= UPDATE LEAD =================
    @PutMapping("/{id}")
    public ResponseEntity<LeadDto> updateLead(
            @PathVariable int id,
            @RequestBody LeadDto dto) {

        return ResponseEntity.ok(leadService.updateLead(id, dto));
    }

    // ================= DELETE LEAD =================
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteLead(@PathVariable int id) {
        leadService.deleteLead(id);
        return ResponseEntity.ok("Lead deleted successfully");
    }

    @PostMapping("/import")
    public ResponseEntity<Map<String, Object>> importLeads(
            @RequestBody Map<String, List<LeadDto>> payload) {

        return ResponseEntity.ok(
                leadService.bulkImportLeads(payload.get("leads")));
    }

    @PostMapping("/from-inquiry")
    public ResponseEntity<LeadDto> createLeadFromInquiry(
            @Valid @RequestBody InquiryLeadCreateDto dto) {

        return ResponseEntity.ok(
                leadService.createFromInquiry(dto));
    }

}

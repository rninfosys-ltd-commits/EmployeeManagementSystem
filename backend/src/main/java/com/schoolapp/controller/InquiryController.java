package com.schoolapp.controller;

//import com.Crmemp.dto.request.InquiryDto;
//import com.Crmemp.dto.request.InquiryImportRequest;
//import com.Crmemp.entity.Inquiry;
//import com.Crmemp.repository.InquiryRepository;
//import com.Crmemp.services.InquiryService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.schoolapp.dao.InquiryDto;
import com.schoolapp.dao.InquiryImportRequest;
import com.schoolapp.service.InquiryService;

import java.util.List;

@RestController
@RequestMapping("/api/inquiries")

public class InquiryController {

    private final InquiryService inquiryService;

    public InquiryController(InquiryService inquiryService) {
        this.inquiryService = inquiryService;
    }

    // ✅ CREATE
    @PostMapping
    public ResponseEntity<InquiryDto> create(@RequestBody InquiryDto dto) {
        return ResponseEntity.ok(inquiryService.createInquiry(dto));
    }

    // ✅ GET ALL
    @GetMapping
    public ResponseEntity<List<InquiryDto>> getAll() {
        return ResponseEntity.ok(inquiryService.getAllInquiries());
    }

    // ✅ GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<InquiryDto> getById(@PathVariable int id) {
        return ResponseEntity.ok(inquiryService.getInquiryById(id));
    }

    // ✅ UPDATE (THIS WILL UPDATE, NOT INSERT)
    @PutMapping("/{id}")
    public ResponseEntity<InquiryDto> update(
            @PathVariable int id,
            @RequestBody InquiryDto dto) {

        return ResponseEntity.ok(inquiryService.updateInquiry(id, dto));
    }

    // ✅ DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        inquiryService.deleteInquiry(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/import")
    public ResponseEntity<?> importInquiries(
            @RequestBody InquiryImportRequest request) {
        return ResponseEntity.ok(
                inquiryService.importInquiries(request));
    }

}

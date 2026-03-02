package com.schoolapp.controller;

//import com.Crmemp.entity.KmBatch;
//import com.Crmemp.entity.KmDetails;
//import com.Crmemp.services.KmBatchService;
//import com.Crmemp.services.UserDetailsImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.schoolapp.entity.KmBatch;
import com.schoolapp.entity.KmDetails;
import com.schoolapp.service.KmBatchService;

import java.util.List;

@RestController
@RequestMapping("/api/km-batch")

public class KmBatchController {

    @Autowired
    private KmBatchService kmBatchService;

    // Create batch: POST /api/km-batch (aligned with frontend)
    @PostMapping
    public KmBatch createBatch(@RequestBody KmBatch batch) {
        return kmBatchService.createBatch(batch);
    }

    // Get all batches
    @GetMapping
    public List<KmBatch> getAllBatches() {
        return kmBatchService.getAllBatches();
    }

    // Alternative endpoint used by earlier frontends: /all
    @GetMapping("/all")
    public List<KmBatch> getAllBatchesAlt() {
        return kmBatchService.getAllBatches();
    }

    // Get entries for batch (frontend uses /{batchNo}/entries)
    @GetMapping("/{batchNo}/entries")
    public List<KmDetails> getEntries(@PathVariable Long batchNo) {
        return kmBatchService.getEntries(batchNo);
    }

    // Approve: uses authenticated user
    // @PostMapping("/{batchNo}/approve")
    // public KmBatch approve(@PathVariable Long batchNo) {
    // Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    // if (auth == null || !(auth.getPrincipal() instanceof UserDetailsImpl)) {
    // throw new RuntimeException("Unauthenticated");
    // }
    // UserDetailsImpl u = (UserDetailsImpl) auth.getPrincipal();
    // return kmBatchService.approveBatch(batchNo, u.getId(), u.getRole());
    // }
    @PostMapping("/{batchNo}/approve")
    public KmBatch approve(@PathVariable Long batchNo,
            @RequestParam Long userId,
            @RequestParam String role) {

        return kmBatchService.approveBatch(batchNo, userId, role);
    }

    // Reject: reason optional (query param)
    // @PostMapping("/{batchNo}/reject")
    // public KmBatch reject(@PathVariable Long batchNo, @RequestParam(required =
    // false) String reason) {
    // Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    // if (auth == null || !(auth.getPrincipal() instanceof UserDetailsImpl)) {
    // throw new RuntimeException("Unauthenticated");
    // }
    // UserDetailsImpl u = (UserDetailsImpl) auth.getPrincipal();
    // return kmBatchService.rejectBatch(batchNo, u.getId(), u.getRole(), reason);
    // }

    @PostMapping("/{batchNo}/reject")
    public KmBatch reject(@PathVariable Long batchNo,
            @RequestParam Long userId,
            @RequestParam String role,
            @RequestParam(required = false) String reason) {

        return kmBatchService.rejectBatch(batchNo, userId, role, reason);
    }

    // Delete batch
    @DeleteMapping("/{batchNo}")
    public ResponseEntity<?> delete(@PathVariable Long batchNo) {
        kmBatchService.deleteBatch(batchNo);
        return ResponseEntity.noContent().build();
    }

    // Download PDF endpoints (two variants)
    // @GetMapping("/{batchNo}/pdf")
    // public ResponseEntity<byte[]> downloadPdf(@PathVariable Long batchNo) {
    // byte[] pdf = kmBatchService.generatePdf(batchNo);
    // return ResponseEntity.ok()
    // .header("Content-Type", "application/pdf")
    // .header("Content-Disposition", "attachment; filename=KM-Batch-" + batchNo +
    // ".pdf")
    // .body(pdf);
    // }
    //
    // @GetMapping("/{batchNo}/download")
    // public ResponseEntity<byte[]> downloadPdfAlias(@PathVariable Long batchNo) {
    // return downloadPdf(batchNo);
    // }
}

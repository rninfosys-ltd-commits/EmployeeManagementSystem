package com.schoolapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.schoolapp.entity.BatchDetails;
import com.schoolapp.service.BatchDetailsService;

@RestController
@RequestMapping("/api/batch")

public class BatchDetailsController {

    @Autowired
    private BatchDetailsService batchService;

    @PostMapping
    public ResponseEntity<?> createBatch(@RequestBody BatchDetails batch) {
        return ResponseEntity.status(201).body(batchService.createBatch(batch));
    }

    @GetMapping
    public ResponseEntity<?> getAllBatches() {
        return ResponseEntity.ok(batchService.getAllBatches());
    }

    @PostMapping("/{bactno}/approve")
    public ResponseEntity<?> approve(@PathVariable Long bactno) {

        return ResponseEntity.ok(batchService.approveBatch(bactno, 1L, ""));
    }

    @PostMapping("/{bactno}/reject")
    public ResponseEntity<?> reject(@PathVariable Long bactno,
            @RequestParam(required = false) String reason) {

        return ResponseEntity.ok(batchService.rejectBatch(bactno, 1L, "", reason));
    }

    @PutMapping("/{bactno}")
    public ResponseEntity<?> update(@PathVariable Long bactno,
            @RequestBody BatchDetails update) {

        return ResponseEntity.ok(batchService.updateBatch(bactno, update, 1L, ""));
    }

    @DeleteMapping("/{bactno}")
    public ResponseEntity<?> delete(@PathVariable Long bactno) {

        batchService.deleteBatch(bactno, 1L, "");
        return ResponseEntity.noContent().build();
    }

}

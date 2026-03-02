package com.schoolapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.schoolapp.service.WorkflowValidationService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/workflow")
@CrossOrigin(origins = "*")
public class WorkflowController {

    @Autowired
    private WorkflowValidationService workflowService;

    /**
     * GET /api/workflow/status/{batchNo}
     * Returns workflow status for a batch
     * e.g. { "PRODUCTION": true, "CASTING": true, "CUTTING": false, ... }
     */
    @GetMapping("/status/{batchNo}")
    public ResponseEntity<Map<String, Boolean>> getWorkflowStatus(@PathVariable String batchNo) {
        Map<String, Boolean> status = workflowService.getWorkflowStatus(batchNo);
        return ResponseEntity.ok(status);
    }

    /**
     * GET /api/workflow/can-proceed/{batchNo}/{targetStage}
     * Returns whether the batch can proceed to the target stage
     */
    @GetMapping("/can-proceed/{batchNo}/{targetStage}")
    public ResponseEntity<Map<String, Object>> canProceed(
            @PathVariable String batchNo,
            @PathVariable String targetStage) {

        boolean allowed = workflowService.canProceedToStage(batchNo, targetStage);
        String previousStage = workflowService.getPreviousStageName(targetStage);

        return ResponseEntity.ok(Map.of(
                "allowed", allowed,
                "targetStage", targetStage.toUpperCase(),
                "previousStage", previousStage != null ? previousStage : "NONE",
                "message", allowed ? "OK" : "Previous stage (" + previousStage + ") must be completed first"));
    }

    /**
     * GET /api/workflow/batches/incomplete
     * Returns only IN_PROGRESS batch numbers (descending)
     */
    @GetMapping("/batches/incomplete")
    public ResponseEntity<List<String>> getIncompleteBatches() {
        List<String> batches = workflowService.getIncompleteBatchNumbers();
        return ResponseEntity.ok(batches);
    }

    /**
     * GET /api/workflow/batches/all-status
     * Returns workflow status for ALL batches
     */
    @GetMapping("/batches/all-status")
    public ResponseEntity<List<Map<String, Object>>> getAllBatchStatus() {
        List<String> allBatches = workflowService.getAllBatchNumbers();

        List<Map<String, Object>> result = allBatches.stream().map(batchNo -> {
            Map<String, Boolean> status = workflowService.getWorkflowStatus(batchNo);
            boolean completed = workflowService.isBatchFullyCompleted(batchNo);

            return Map.<String, Object>of(
                    "batchNo", batchNo,
                    "stages", status,
                    "completed", completed);
        }).collect(java.util.stream.Collectors.toList());

        return ResponseEntity.ok(result);
    }
}

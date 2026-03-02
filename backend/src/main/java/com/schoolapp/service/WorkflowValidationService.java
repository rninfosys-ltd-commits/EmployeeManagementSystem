package com.schoolapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.schoolapp.repository.ProductionEntryRepository;
import com.schoolapp.repository.CastingHallReportRepository;
import com.schoolapp.repository.WireCuttingReportRepository;
import com.schoolapp.repository.AutoclaveRepository;
import com.schoolapp.repository.BlockSeparatingRepository;
import com.schoolapp.repository.CubeTestRepository;
import com.schoolapp.repository.RejectionDataRepository;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class WorkflowValidationService {

    @Autowired
    private ProductionEntryRepository productionRepo;
    @Autowired
    private CastingHallReportRepository castingRepo;
    @Autowired
    private WireCuttingReportRepository cuttingRepo;
    @Autowired
    private AutoclaveRepository autoclaveRepo;
    @Autowired
    private BlockSeparatingRepository blockRepo;
    @Autowired
    private CubeTestRepository cubeRepo;
    @Autowired
    private RejectionDataRepository rejectionRepo;

    // Ordered stage names
    private static final List<String> STAGES = Arrays.asList(
            "PRODUCTION", "CASTING", "CUTTING", "AUTOCLAVE",
            "BLOCK_SEPARATING", "CUBE_TEST", "REJECTION");

    /**
     * Check if a batch has completed a specific stage
     */
    public boolean isStageCompleted(String batchNo, String stageName) {
        switch (stageName.toUpperCase()) {
            case "PRODUCTION":
                return productionRepo.existsByBatchNo(batchNo);
            case "CASTING":
                return castingRepo.existsByBatchNo(batchNo);
            case "CUTTING":
                return cuttingRepo.existsByBatchNo(batchNo);
            case "AUTOCLAVE":
                try {
                    int batchInt = Integer.parseInt(batchNo);
                    return autoclaveRepo.existsByWagonBatch(batchInt);
                } catch (NumberFormatException e) {
                    return false;
                }
            case "BLOCK_SEPARATING":
                return blockRepo.existsByBatchNumber(batchNo);
            case "CUBE_TEST":
                return cubeRepo.existsByBatchNo(batchNo);
            case "REJECTION":
                return rejectionRepo.existsByBatchNo(batchNo);
            default:
                return false;
        }
    }

    /**
     * Check if a batch can proceed to a target stage
     * (previous stage must be completed)
     */
    public boolean canProceedToStage(String batchNo, String targetStage) {
        int targetIdx = STAGES.indexOf(targetStage.toUpperCase());

        // Production is always allowed (first stage)
        if (targetIdx <= 0)
            return true;

        // Check previous stage
        String previousStage = STAGES.get(targetIdx - 1);
        return isStageCompleted(batchNo, previousStage);
    }

    /**
     * Get the name of the previous stage
     */
    public String getPreviousStageName(String targetStage) {
        int targetIdx = STAGES.indexOf(targetStage.toUpperCase());
        if (targetIdx <= 0)
            return null;
        return STAGES.get(targetIdx - 1);
    }

    /**
     * Get full workflow status for a batch
     * Returns ordered map of stage → completed boolean
     */
    public Map<String, Boolean> getWorkflowStatus(String batchNo) {
        Map<String, Boolean> status = new LinkedHashMap<>();
        for (String stage : STAGES) {
            status.put(stage, isStageCompleted(batchNo, stage));
        }
        return status;
    }

    /**
     * Check if ALL stages are completed for a batch
     */
    public boolean isBatchFullyCompleted(String batchNo) {
        for (String stage : STAGES) {
            if (!isStageCompleted(batchNo, stage)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Get all unique batch numbers from Production
     */
    public List<String> getAllBatchNumbers() {
        return productionRepo.findAll().stream()
                .map(p -> p.getBatchNo())
                .distinct()
                .collect(java.util.stream.Collectors.toList());
    }

    /**
     * Get only incomplete (in-progress) batch numbers
     */
    public List<String> getIncompleteBatchNumbers() {
        return getAllBatchNumbers().stream()
                .filter(b -> !isBatchFullyCompleted(b))
                .collect(java.util.stream.Collectors.toList());
    }
}

package com.schoolapp.service;

import org.springframework.stereotype.Service;

import com.schoolapp.dao.WireCuttingImportRequestDto;
import com.schoolapp.dao.WireCuttingImportResponseDto;
import com.schoolapp.dao.WireCuttingImportResultDto;
import com.schoolapp.dao.WireCuttingReportRequestDto;
import com.schoolapp.entity.WireCuttingReport;
import com.schoolapp.repository.WireCuttingReportRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class WireCuttingReportServiceImpl implements WireCuttingReportService {

    private final WireCuttingReportRepository repository;

    public WireCuttingReportServiceImpl(WireCuttingReportRepository repository) {
        this.repository = repository;
    }

    @Override
    public WireCuttingReport save(WireCuttingReportRequestDto dto) {
        WireCuttingReport r = new WireCuttingReport();
        map(dto, r);
        r.setApprovalStage("NONE");
        return repository.save(r);
    }

    @Override
    public WireCuttingReport update(Long id, WireCuttingReportRequestDto dto) {
        WireCuttingReport r = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Not found"));
        map(dto, r);
        return repository.save(r);
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    public List<WireCuttingReport> getAll() {
        return repository.findAll();
    }

    // ✅ APPROVE (no security context)
    @Override
    public WireCuttingReport approve(Long id, Long userId, String role, String username) {

        WireCuttingReport r = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Wire cutting report not found"));

        String stage = r.getApprovalStage();
        if (stage == null)
            stage = "NONE";

        switch (stage) {

            case "NONE":
                if (!"ROLE_DIRECTOR".equals(role))
                    throw new RuntimeException("Only L1 can approve");
                r.setApprovedByL1(username);
                r.setApprovalStage("L1");
                break;

            case "L1":
                if (!"ROLE_MANAGER".equals(role))
                    throw new RuntimeException("Only L2 can approve");
                r.setApprovedByL2(username);
                r.setApprovalStage("L2");
                break;

            case "L2":
                if (!"ROLE_SUPERVISOR".equals(role))
                    throw new RuntimeException("Only L3 can approve");
                r.setApprovedByL3(username);
                r.setApprovalStage("L3");
                break;

            default:
                throw new RuntimeException("Already final approved");
        }

        return repository.save(r);
    }

    // ✅ REJECT
    @Override
    public WireCuttingReport reject(Long id, Long userId, String role, String username, String reason) {

        WireCuttingReport r = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Wire cutting report not found"));

        String stage = r.getApprovalStage();

        if ("L3".equals(stage))
            throw new RuntimeException("Final approved cannot be rejected");

        if (("NONE".equals(stage) && !"ROLE_DIRECTOR".equals(role)) ||
                ("L1".equals(stage) && !"ROLE_MANAGER".equals(role)) ||
                ("L2".equals(stage) && !"ROLE_SUPERVISOR".equals(role))) {
            throw new RuntimeException("No authority to reject");
        }

        r.setApprovalStage("NONE");
        r.setApprovedByL1(null);
        r.setApprovedByL2(null);
        r.setApprovedByL3(null);
        r.setRejectionReason(reason);

        return repository.save(r);
    }

    private void map(WireCuttingReportRequestDto d, WireCuttingReport r) {
        r.setBatchNo(d.getBatchNo());
        r.setCuttingDate(d.getCuttingDate());
        r.setMouldNo(d.getMouldNo());
        r.setSize(d.getSize());
        r.setBallTestMm(d.getBallTestMm());
        r.setOtherReason(d.getOtherReason());
        r.setTime(d.getTime());
        r.setUserId(d.getUserId());
        r.setBranchId(d.getBranchId());
        r.setOrgId(d.getOrgId());
    }

    @Override
    public WireCuttingImportResponseDto importWireCutting(WireCuttingImportRequestDto dto) {

        int saved = 0;
        int failed = 0;
        List<WireCuttingImportResultDto> results = new ArrayList<>();

        for (WireCuttingReportRequestDto row : dto.getWireCuttings()) {

            try {
                if (repository.existsByBatchNo(row.getBatchNo())) {
                    failed++;
                    results.add(new WireCuttingImportResultDto(
                            row.getBatchNo(),
                            "FAILED",
                            "Duplicate batch number"));
                    continue;
                }

                WireCuttingReport entity = new WireCuttingReport();
                map(row, entity);
                entity.setApprovalStage("NONE");

                repository.save(entity);

                saved++;
                results.add(new WireCuttingImportResultDto(
                        row.getBatchNo(),
                        "SAVED",
                        null));

            } catch (Exception e) {
                failed++;
                results.add(new WireCuttingImportResultDto(
                        row.getBatchNo(),
                        "FAILED",
                        e.getMessage()));
            }
        }

        WireCuttingImportResponseDto response = new WireCuttingImportResponseDto();
        response.setSavedCount(saved);
        response.setErrorCount(failed);
        response.setResults(results);

        return response;
    }
}

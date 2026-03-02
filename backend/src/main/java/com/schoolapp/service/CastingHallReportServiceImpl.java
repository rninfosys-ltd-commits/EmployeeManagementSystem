package com.schoolapp.service;

// import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.schoolapp.entity.CastingHallReport;
import com.schoolapp.repository.CastingHallReportRepository;

import com.schoolapp.dao.CastingHallReportRequestDto;
import com.schoolapp.dao.CastingImportRequestDto;
import com.schoolapp.dao.CastingImportResponse;
import com.schoolapp.dao.CastingImportResult;

import java.util.ArrayList;
import java.util.List;

@Service
public class CastingHallReportServiceImpl implements CastingHallReportService {

    private final CastingHallReportRepository repository;

    public CastingHallReportServiceImpl(CastingHallReportRepository repository) {
        this.repository = repository;
    }

    @Override
    public CastingHallReport save(CastingHallReportRequestDto dto) {
        CastingHallReport report = new CastingHallReport();
        mapDtoToEntity(dto, report);
        return repository.save(report);
    }

    @Override
    public CastingHallReport update(Long id, CastingHallReportRequestDto dto) {
        CastingHallReport report = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Not found"));
        mapDtoToEntity(dto, report);
        return repository.save(report);
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    public List<CastingHallReport> getAll() {
        return repository.findAll();
    }

    @Override
    public CastingHallReport approve(Long id, Long userId, String role) {

        CastingHallReport report = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Casting report not found"));

        String stage = report.getApprovalStage();
        if (stage == null)
            stage = "NONE";

        switch (stage) {

            case "NONE":
                if (!"ROLE_DIRECTOR".equals(role))
                    throw new RuntimeException("Only L1 can approve");
                report.setApprovedByL1(String.valueOf(userId));
                report.setApprovalStage("L1");
                break;

            case "L1":
                if (!"ROLE_MANAGER".equals(role))
                    throw new RuntimeException("Only L2 can approve");
                report.setApprovedByL2(String.valueOf(userId));
                report.setApprovalStage("L2");
                break;

            case "L2":
                if (!"ROLE_SUPERVISOR".equals(role))
                    throw new RuntimeException("Only L3 can approve");
                report.setApprovedByL3(String.valueOf(userId));
                report.setApprovalStage("L3");
                break;

            default:
                throw new RuntimeException("Already approved");
        }

        return repository.save(report);
    }

    @Override
    public CastingHallReport reject(Long id, Long userId, String role, String reason) {

        CastingHallReport report = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Casting report not found"));

        String stage = report.getApprovalStage();

        if ("L3".equals(stage))
            throw new RuntimeException("Final approved cannot be rejected");

        if (("NONE".equals(stage) && !"ROLE_DIRECTOR".equals(role)) ||
                ("L1".equals(stage) && !"ROLE_MANAGER".equals(role)) ||
                ("L2".equals(stage) && !"ROLE_SUPERVISOR".equals(role))) {
            throw new RuntimeException("No authority to reject");
        }

        report.setApprovalStage("NONE");
        report.setApprovedByL1(null);
        report.setApprovedByL2(null);
        report.setApprovedByL3(null);
        report.setRejectedBy(String.valueOf(userId));
        report.setRejectionReason(reason);

        return repository.save(report);
    }

    private void mapDtoToEntity(CastingHallReportRequestDto dto, CastingHallReport r) {
        r.setBatchNo(dto.getBatchNo());
        r.setSize(dto.getSize());
        r.setBedNo(dto.getBedNo());
        r.setMouldNo(dto.getMouldNo());
        r.setCastingTime(dto.getCastingTime());
        r.setConsistency(dto.getConsistency());
        r.setFlowInCm(dto.getFlowInCm());
        r.setCastingTempC(dto.getCastingTempC());
        r.setVt(dto.getVt());
        r.setMassTemp(dto.getMassTemp());
        r.setFallingTestMm(dto.getFallingTestMm());
        r.setTestTime(dto.getTestTime());
        r.setHTime(dto.getHTime());
        r.setRemark(dto.getRemark());
        r.setUserId(dto.getUserId());
        r.setBranchId(dto.getBranchId());
        r.setOrgId(dto.getOrgId());
    }

    @Override
    public CastingHallReport getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Casting report not found"));
    }

    @Override
    public CastingImportResponse importCasting(CastingImportRequestDto dto) {

        int saved = 0;
        int failed = 0;
        List<CastingImportResult> results = new ArrayList<>();

        if (dto.getCastings() == null || dto.getCastings().isEmpty()) {
            throw new RuntimeException("No casting data provided");
        }

        for (CastingHallReportRequestDto row : dto.getCastings()) {
            try {
                // ❌ Skip duplicate batch
                if (repository.existsByBatchNo(row.getBatchNo())) {
                    failed++;
                    results.add(
                            new CastingImportResult(
                                    row.getBatchNo(),
                                    "FAILED",
                                    "Duplicate batch number"));
                    continue;
                }

                CastingHallReport report = new CastingHallReport();
                mapDtoToEntity(row, report);

                report.setApprovalStage("NONE");
                repository.save(report);

                saved++;
                results.add(
                        new CastingImportResult(
                                row.getBatchNo(),
                                "SUCCESS",
                                null));

            } catch (Exception e) {
                failed++;
                results.add(
                        new CastingImportResult(
                                row.getBatchNo(),
                                "FAILED",
                                e.getMessage()));
            }
        }

        return new CastingImportResponse(saved, failed, results);
    }

}

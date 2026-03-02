package com.schoolapp.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.schoolapp.dao.MaterialValueDTO;
import com.schoolapp.dao.ProductionEntryRequest;
import com.schoolapp.dao.ProductionImportRequest;
import com.schoolapp.dao.ProductionImportResponse;
import com.schoolapp.dao.ProductionImportResult;
import com.schoolapp.entity.ProductionEntry;
import com.schoolapp.entity.ProductionMaterial;
import com.schoolapp.repository.ProductionEntryRepository;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ProductionEntryServiceImpl implements ProductionEntryService {

    private final ProductionEntryRepository repository;

    public ProductionEntryServiceImpl(
            ProductionEntryRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public ProductionEntry save(ProductionEntryRequest r) {

        ProductionEntry e = new ProductionEntry();
        mapRequestToEntity(r, e);
        e.setApprovalStage("NONE");

        // 1️⃣ Save first to generate ID
        ProductionEntry saved = repository.save(e);

        // 2️⃣ Set batchNo = id
        saved.setBatchNo(String.valueOf(saved.getId()));

        // 3️⃣ Save dynamic materials
        saveMaterials(saved, r.getMaterials());

        // 4️⃣ Save again
        return repository.save(saved);
    }

    @Override
    @Transactional
    public ProductionEntry update(Long id, ProductionEntryRequest r) {

        ProductionEntry e = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Production entry not found"));

        mapRequestToEntity(r, e);

        // Update dynamic materials — clear old, insert new
        e.getMaterials().clear();
        repository.save(e); // flush orphan removal

        saveMaterials(e, r.getMaterials());

        return repository.save(e);
    }

    private void saveMaterials(ProductionEntry entry, List<MaterialValueDTO> materials) {
        if (materials == null || materials.isEmpty())
            return;

        List<ProductionMaterial> list = new ArrayList<>();
        for (MaterialValueDTO dto : materials) {
            ProductionMaterial pm = ProductionMaterial.builder()
                    .productionEntry(entry)
                    .materialMasterId(dto.getMaterialMasterId())
                    .materialName(dto.getMaterialName())
                    .unit(dto.getUnit())
                    .value(dto.getValue())
                    .displayOrder(dto.getDisplayOrder())
                    .build();
            list.add(pm);
        }
        entry.getMaterials().addAll(list);
    }

    private void mapRequestToEntity(ProductionEntryRequest r, ProductionEntry e) {

        e.setShift(r.shift);

        e.setSiloNo1(r.siloNo1);
        e.setLiterWeight1(r.literWeight1);
        e.setFaSolid1(r.faSolid1);

        e.setSiloNo2(r.siloNo2);
        e.setLiterWeight2(r.literWeight2);
        e.setFaSolid2(r.faSolid2);

        // 🔥 AUTO TOTAL SOLID
        double totalSolid = (r.faSolid1 != null ? r.faSolid1 : 0) +
                (r.faSolid2 != null ? r.faSolid2 : 0);
        e.setTotalSolid(totalSolid);

        // Legacy material fields — still mapped
        e.setWaterLiter(r.waterLiter);
        e.setCementKg(r.cementKg);
        e.setLimeKg(r.limeKg);
        e.setGypsumKg(r.gypsumKg);
        e.setSolOilKg(r.solOilKg);
        e.setAiPowerGm(r.aiPowerGm);
        e.setTempC(r.tempC);

        e.setCastingTime(r.castingTime);
        e.setProductionTime(r.productionTime);
        e.setProductionRemark(r.productionRemark);
        e.setRemark(r.remark);

        e.setUserId(r.userId);
        e.setBranchId(r.branchId);
        e.setOrgId(r.orgId);
    }

    @Override
    public List<ProductionEntry> getAll() {
        return repository.findAll();
    }

    @Override
    public ProductionEntry getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Production entry not found"));
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    public ProductionEntry approve(Long productionId, Long userId, String role) {

        ProductionEntry entry = repository.findById(productionId)
                .orElseThrow(() -> new RuntimeException("Production entry not found"));

        String stage = entry.getApprovalStage();
        if (stage == null)
            stage = "NONE";

        switch (stage) {

            case "NONE":
                if (!"ROLE_DIRECTOR".equals(role))
                    throw new RuntimeException("Only L1 can approve");

                entry.setApprovedByL1(String.valueOf(userId));
                entry.setApprovalTimeL1(now());
                entry.setApprovalStage("L1");
                break;

            case "L1":
                if (!"ROLE_MANAGER".equals(role))
                    throw new RuntimeException("Only L2 can approve");

                entry.setApprovedByL2(String.valueOf(userId));
                entry.setApprovalTimeL2(now());
                entry.setApprovalStage("L2");
                break;

            case "L2":
                if (!"ROLE_SUPERVISOR".equals(role))
                    throw new RuntimeException("Only L3 can approve");

                entry.setApprovedByL3(String.valueOf(userId));
                entry.setApprovalTimeL3(now());
                entry.setApprovalStage("L3");
                break;

            default:
                throw new RuntimeException("Already approved");
        }

        return repository.save(entry);
    }

    @Override
    public ProductionEntry reject(Long productionId, Long userId, String role, String reason) {

        ProductionEntry entry = repository.findById(productionId)
                .orElseThrow(() -> new RuntimeException("Production entry not found"));

        String stage = entry.getApprovalStage();
        if (stage == null)
            stage = "NONE";

        if ("L3".equals(stage))
            throw new RuntimeException("Final approved cannot be rejected");

        if (("NONE".equals(stage) && !"ROLE_DIRECTOR".equals(role)) ||
                ("L1".equals(stage) && !"ROLE_MANAGER".equals(role)) ||
                ("L2".equals(stage) && !"ROLE_SUPERVISOR".equals(role))) {
            throw new RuntimeException("No authority to reject");
        }

        entry.setApprovalStage("NONE");
        entry.setApprovedByL1(null);
        entry.setApprovedByL2(null);
        entry.setApprovedByL3(null);
        entry.setRejectedBy(String.valueOf(userId));
        entry.setRejectReason(reason);

        return repository.save(entry);
    }

    @Override
    @Transactional
    public ProductionImportResponse importProduction(
            ProductionImportRequest request) {

        int saved = 0;
        int failed = 0;

        List<ProductionImportResult> results = new ArrayList<>();
        if (request.getProductions() == null || request.getProductions().isEmpty()) {
            throw new RuntimeException("No production data received");
        }
        for (ProductionEntryRequest r : request.getProductions()) {
            try {

                r.setUserId(request.getUploadedBy());
                r.setBranchId(request.getBranchId());
                r.setOrgId(request.getOrgId());

                ProductionEntry savedEntry = this.save(r);

                results.add(
                        new ProductionImportResult(
                                savedEntry.getBatchNo(),
                                "SUCCESS",
                                null));
                saved++;

            } catch (Exception ex) {

                results.add(
                        new ProductionImportResult(
                                r.getShift(),
                                "FAILED",
                                ex.getMessage()));
                failed++;
            }
        }

        return new ProductionImportResponse(saved, failed, results);
    }

    @Override
    public Map<String, Object> importExcel(Map<String, Object> body) {

        @SuppressWarnings("unchecked")
        List<Map<String, Object>> rows = (List<Map<String, Object>>) body.get("rows");

        int saved = 0;
        int failed = 0;
        List<Map<String, String>> results = new ArrayList<>();

        for (Map<String, Object> row : rows) {
            try {
                ProductionEntry e = new ProductionEntry();

                e.setShift(get(row, "Shift"));
                e.setBatchNo(get(row, "Batch No"));

                e.setSiloNo1(get(row, "Silo No 1"));
                e.setLiterWeight1(toDouble(row.get("Liter Weight 1")));
                e.setFaSolid1(toDouble(row.get("FA Solid 1")));

                e.setSiloNo2(get(row, "Silo No 2"));
                e.setLiterWeight2(toDouble(row.get("Liter Weight 2")));
                e.setFaSolid2(toDouble(row.get("FA Solid 2")));

                e.setWaterLiter(toDouble(row.get("Water Liter")));
                e.setCementKg(toDouble(row.get("Cement Kg")));
                e.setLimeKg(toDouble(row.get("Lime Kg")));
                e.setGypsumKg(toDouble(row.get("Gypsum Kg")));
                e.setSolOilKg(toDouble(row.get("Sol Oil Kg")));
                e.setAiPowerGm(toDouble(row.get("AI Power gm")));
                e.setTempC(toDouble(row.get("Temperature")));

                e.setCastingTime(get(row, "Casting Time"));
                e.setProductionTime(get(row, "Production Time"));
                e.setProductionRemark(get(row, "Production Remark"));
                e.setRemark(get(row, "Remark"));

                repository.save(e);
                saved++;

                results.add(Map.of("status", "SUCCESS"));

            } catch (Exception ex) {
                failed++;
                results.add(Map.of(
                        "status", "FAILED",
                        "error", ex.getMessage()));
            }
        }

        return Map.of(
                "saved", saved,
                "failed", failed,
                "results", results);
    }

    private String get(Map<String, Object> row, String key) {
        Object v = row.get(key);
        return v == null ? null : v.toString();
    }

    private Double toDouble(Object v) {
        if (v == null || v.toString().isEmpty())
            return null;
        return Double.parseDouble(v.toString());
    }

    private String now() {
        return LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));
    }

}
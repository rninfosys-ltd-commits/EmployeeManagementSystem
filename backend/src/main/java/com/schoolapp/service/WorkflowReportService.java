package com.schoolapp.service;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.schoolapp.entity.*;
import com.schoolapp.repository.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class WorkflowReportService {

    @Autowired
    private ProductionEntryRepository productionRepo;
    @Autowired
    private CastingHallReportRepository castingRepo;
    @Autowired
    private WireCuttingReportRepository cuttingRepo;
    @Autowired
    private BlockSeparatingRepository blockRepo;
    @Autowired
    private CubeTestRepository cubeRepo;
    @Autowired
    private RejectionDataRepository rejectionRepo;
    @Autowired
    private AutoclaveRepository autoclaveRepo;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final List<String> STAGES = Arrays.asList(
            "PRODUCTION", "CASTING", "CUTTING", "AUTOCLAVE",
            "BLOCK_SEPARATING", "CUBE_TEST", "REJECTION");

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    private final Font titleFont = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD, BaseColor.DARK_GRAY);
    private final Font sectionFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, new BaseColor(33, 97, 140));
    private final Font normalFont = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL);

    /**
     * Generate combined PDF up to a specific stage for a batch
     */
    public byte[] generateReport(String batchNo, String upToStage) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4.rotate());
        PdfWriter.getInstance(document, baos);
        document.open();

        // Title
        Paragraph title = new Paragraph("Production Workflow Report", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);

        Paragraph batchPara = new Paragraph("Batch No: " + batchNo, sectionFont);
        batchPara.setAlignment(Element.ALIGN_CENTER);
        batchPara.setSpacingAfter(15);
        document.add(batchPara);

        int targetIdx = STAGES.indexOf(upToStage.toUpperCase());
        if (targetIdx < 0)
            targetIdx = STAGES.size() - 1;

        // Add each stage up to the target
        for (int i = 0; i <= targetIdx; i++) {
            String stage = STAGES.get(i);
            addStageSection(document, batchNo, stage);
        }

        document.close();
        return baos.toByteArray();
    }

    /**
     * Generate flat EXCEL for all stages of a batch
     */
    public byte[] generateExcelReport(String batchNo, String upToStage) throws IOException {
        return generateHorizontalExcel(null, null, batchNo, upToStage);
    }

    /**
     * Generate flat EXCEL for all batches in a date range
     */
    public byte[] generateFlatWorkflowExcelReport(Date fromDate, Date toDate, String upToStage) throws IOException {
        return generateHorizontalExcel(fromDate, toDate, null, upToStage, null);
    }

    public byte[] generateFlatWorkflowExcelReport(Date fromDate, Date toDate, String upToStage, String plantName)
            throws IOException {
        return generateHorizontalExcel(fromDate, toDate, null, upToStage, plantName);
    }

    private List<String> getBatchesForStageInRange(Date from, Date to, String stage, String plantName) {
        switch (stage.toUpperCase()) {
            case "PRODUCTION":
                List<ProductionEntry> prodEntries = (plantName != null && !plantName.isBlank())
                        ? productionRepo.findByCreatedDateBetweenAndPlantName(from, to, plantName)
                        : productionRepo.findByCreatedDateBetween(from, to);
                return prodEntries.stream()
                        .map(ProductionEntry::getBatchNo).filter(Objects::nonNull).distinct().sorted().toList();
            case "CASTING":
                if (plantName != null && !plantName.isBlank()) {
                    // Filter casting batches via production plant
                    List<String> plantBatches = productionRepo.findByPlantName(plantName).stream()
                            .map(ProductionEntry::getBatchNo).filter(Objects::nonNull).distinct().toList();
                    return castingRepo.findByCreatedDateBetween(from, to).stream()
                            .map(CastingHallReport::getBatchNo).filter(b -> b != null && plantBatches.contains(b))
                            .distinct().sorted().toList();
                }
                return castingRepo.findByCreatedDateBetween(from, to).stream()
                        .map(CastingHallReport::getBatchNo).filter(Objects::nonNull).distinct().sorted().toList();
            case "CUTTING":
                if (plantName != null && !plantName.isBlank()) {
                    List<String> plantBatches = productionRepo.findByPlantName(plantName).stream()
                            .map(ProductionEntry::getBatchNo).filter(Objects::nonNull).distinct().toList();
                    return cuttingRepo.findByCreatedDateBetween(from, to).stream()
                            .map(WireCuttingReport::getBatchNo).filter(b -> b != null && plantBatches.contains(b))
                            .distinct().sorted().toList();
                }
                return cuttingRepo.findByCreatedDateBetween(from, to).stream()
                        .map(WireCuttingReport::getBatchNo).filter(Objects::nonNull).distinct().sorted().toList();
            case "BLOCK_SEPARATING":
                if (plantName != null && !plantName.isBlank()) {
                    List<String> plantBatches = productionRepo.findByPlantName(plantName).stream()
                            .map(ProductionEntry::getBatchNo).filter(Objects::nonNull).distinct().toList();
                    return blockRepo.findByReportDateBetween(from, to).stream()
                            .map(BlockSeparating::getBatchNumber).filter(b -> b != null && plantBatches.contains(b))
                            .distinct().sorted().toList();
                }
                return blockRepo.findByReportDateBetween(from, to).stream()
                        .map(BlockSeparating::getBatchNumber).filter(Objects::nonNull).distinct().sorted().toList();
            case "CUBE_TEST":
                if (plantName != null && !plantName.isBlank()) {
                    List<String> plantBatches = productionRepo.findByPlantName(plantName).stream()
                            .map(ProductionEntry::getBatchNo).filter(Objects::nonNull).distinct().toList();
                    return cubeRepo.findByTestingDateBetween(from, to).stream()
                            .map(CubeTestEntity::getBatchNo).filter(b -> b != null && plantBatches.contains(b))
                            .distinct().sorted().toList();
                }
                return cubeRepo.findByTestingDateBetween(from, to).stream()
                        .map(CubeTestEntity::getBatchNo).filter(Objects::nonNull).distinct().sorted().toList();
            case "REJECTION":
                if (plantName != null && !plantName.isBlank()) {
                    List<String> plantBatches = productionRepo.findByPlantName(plantName).stream()
                            .map(ProductionEntry::getBatchNo).filter(Objects::nonNull).distinct().toList();
                    return rejectionRepo.findByDateBetween(from, to).stream()
                            .map(RejectionDataEntity::getBatchNo).filter(b -> b != null && plantBatches.contains(b))
                            .distinct().sorted().toList();
                }
                return rejectionRepo.findByDateBetween(from, to).stream()
                        .map(RejectionDataEntity::getBatchNo).filter(Objects::nonNull).distinct().sorted().toList();
            case "CONSOLIDATED":
            default:
                List<ProductionEntry> consolEntries = (plantName != null && !plantName.isBlank())
                        ? productionRepo.findByCreatedDateBetweenAndPlantName(from, to, plantName)
                        : productionRepo.findByCreatedDateBetween(from, to);
                return consolEntries.stream()
                        .map(ProductionEntry::getBatchNo).filter(Objects::nonNull).distinct().sorted().toList();
        }
    }

    /**
     * Generate consolidated EXCEL for all batches in a date range (Flat Cumulative)
     */
    public byte[] generateConsolidatedExcelReport(Date fromDate, Date toDate) throws IOException {
        return generateFlatWorkflowExcelReport(fromDate, toDate, "CONSOLIDATED", null);
    }

    /**
     * Generate PDF report for all batches in a date range (Batch-wise Sequential
     * Stages)
     */
    public byte[] generateFlatWorkflowPdfReport(Date fromDate, Date toDate, String upToStage) throws Exception {
        return generateFlatWorkflowPdfReport(fromDate, toDate, upToStage, null);
    }

    public byte[] generateFlatWorkflowPdfReport(Date fromDate, Date toDate, String upToStage, String plantName)
            throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4.rotate());
        PdfWriter.getInstance(document, baos);
        document.open();

        // Main Title
        String mainTitleStr = upToStage.equalsIgnoreCase("CONSOLIDATED") ? "Consolidated Workflow Report"
                : upToStage + " Production Report";
        if (plantName != null && !plantName.isBlank()) {
            mainTitleStr += " (" + plantName + ")";
        }
        Paragraph mainTitle = new Paragraph(mainTitleStr, titleFont);
        mainTitle.setAlignment(Element.ALIGN_CENTER);
        document.add(mainTitle);

        List<String> batchNumbers = getBatchesForStageInRange(fromDate, toDate, upToStage, plantName);

        for (String batchNo : batchNumbers) {
            Paragraph batchHeader = new Paragraph("BATCH NO: " + batchNo, sectionFont);
            batchHeader.setAlignment(Element.ALIGN_LEFT);
            batchHeader.setSpacingBefore(20);
            batchHeader.setSpacingAfter(10);
            document.add(batchHeader);

            int targetIdx = STAGES.indexOf(upToStage.toUpperCase());
            if (targetIdx < 0)
                targetIdx = STAGES.size() - 1;

            for (int i = 0; i <= targetIdx; i++) {
                addStageSection(document, batchNo, STAGES.get(i));
            }
        }

        document.close();
        return baos.toByteArray();
    }

    private void addStageSection(Document doc, String batchNo, String stage) throws DocumentException {
        Paragraph stageTitle = new Paragraph(formatStageName(stage), sectionFont);
        stageTitle.setSpacingBefore(15);
        stageTitle.setSpacingAfter(5);
        doc.add(stageTitle);

        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.setWidths(new float[] { 35, 65 });

        switch (stage) {
            case "PRODUCTION":
                addProductionData(table, batchNo);
                break;
            case "CASTING":
                addCastingData(table, batchNo);
                break;
            case "CUTTING":
                addCuttingData(table, batchNo);
                break;
            case "AUTOCLAVE":
                addAutoclaveData(table, batchNo);
                break;
            case "BLOCK_SEPARATING":
                addBlockSeparatingData(table, batchNo);
                break;
            case "CUBE_TEST":
                addCubeTestData(table, batchNo);
                break;
            case "REJECTION":
                addRejectionData(table, batchNo);
                break;
        }

        doc.add(table);

        // Divider
        Paragraph divider = new Paragraph(
                "──────────────────────────────────────────────────────────────────────────────────────────────────");
        divider.setSpacingBefore(5);
        doc.add(divider);
    }

    private void addProductionData(PdfPTable table, String batchNo) {
        List<ProductionEntry> entries = productionRepo.findByBatchNo(batchNo);

        if (entries.isEmpty()) {
            addRow(table, "Status", "Not completed");
            return;
        }

        ProductionEntry p = entries.get(0);
        addRow(table, "Date", formatDate(p.getCreatedDate()));
        addRow(table, "Shift", p.getShift() != null ? p.getShift() : "—");
        addRow(table, "Silo 1", p.getSiloNo1() != null ? p.getSiloNo1() : "—");
        addRow(table, "Liter Wt 1", String.valueOf(p.getLiterWeight1()));
        addRow(table, "FA Solid 1", String.valueOf(p.getFaSolid1()));
        addRow(table, "Silo 2", p.getSiloNo2() != null ? p.getSiloNo2() : "—");
        addRow(table, "Liter Wt 2", String.valueOf(p.getLiterWeight2()));
        addRow(table, "FA Solid 2", String.valueOf(p.getFaSolid2()));
        addRow(table, "Total Solid", String.valueOf(p.getTotalSolid()));
        addRow(table, "Water (L)", String.valueOf(p.getWaterLiter()));
        addRow(table, "Cement (kg)", String.valueOf(p.getCementKg()));
        addRow(table, "Lime (kg)", String.valueOf(p.getLimeKg()));
        addRow(table, "Gypsum (kg)", String.valueOf(p.getGypsumKg()));
        addRow(table, "Sol Oil (kg)", String.valueOf(p.getSolOilKg()));
        addRow(table, "Al Power (gm)", String.valueOf(p.getAiPowerGm()));
        addRow(table, "Temp (C)", String.valueOf(p.getTempC()));

        // Materials
        if (p.getMaterials() != null && !p.getMaterials().isEmpty()) {
            for (ProductionMaterial pm : p.getMaterials()) {
                addRow(table, pm.getMaterialName() + " (" + pm.getUnit() + ")", String.valueOf(pm.getValue()));
            }
        }

        addRow(table, "Remark", p.getProductionRemark() != null ? p.getProductionRemark() : "—");
        addRow(table, "Production Time", p.getProductionTime() != null ? p.getProductionTime() : "—");
    }

    private void addCastingData(PdfPTable table, String batchNo) {
        List<CastingHallReport> entries = castingRepo.findByBatchNo(batchNo);

        if (entries.isEmpty()) {
            addRow(table, "Status", "Not completed");
            return;
        }

        CastingHallReport c = entries.get(0);
        addRow(table, "Date", formatDate(c.getCreatedDate()));
        addRow(table, "Shift", c.getShift() != null ? c.getShift() : "—");
        addRow(table, "Size", String.valueOf(c.getSize()));
        addRow(table, "Bed No", String.valueOf(c.getBedNo()));
        addRow(table, "Mould No", String.valueOf(c.getMouldNo()));
        addRow(table, "Casting Time", c.getCastingTime() != null ? c.getCastingTime() : "—");
        addRow(table, "Consistency", String.valueOf(c.getConsistency()));
        addRow(table, "Flow (cm)", String.valueOf(c.getFlowInCm()));
        addRow(table, "Temp (C)", String.valueOf(c.getCastingTempC()));
        addRow(table, "VT", String.valueOf(c.getVt()));
        addRow(table, "Mass Temp", String.valueOf(c.getMassTemp()));
        addRow(table, "Falling Test (mm)", String.valueOf(c.getFallingTestMm()));
        addRow(table, "Test Time", String.valueOf(c.getTestTime()));
        addRow(table, "H Time", String.valueOf(c.getHTime()));
        addRow(table, "Remark", c.getRemark() != null ? c.getRemark() : "—");
    }

    private void addCuttingData(PdfPTable table, String batchNo) {
        List<WireCuttingReport> entries = cuttingRepo.findByBatchNo(batchNo);

        if (entries.isEmpty()) {
            addRow(table, "Status", "Not completed");
            return;
        }

        WireCuttingReport w = entries.get(0);
        addRow(table, "Date", formatDate(w.getCreatedDate()));
        addRow(table, "Shift", w.getShift() != null ? w.getShift() : "—");
        addRow(table, "Cutting Date", formatDate(w.getCuttingDate()));
        addRow(table, "Mould No", String.valueOf(w.getMouldNo()));
        addRow(table, "Size", String.valueOf(w.getSize()));
        addRow(table, "Ball Test (mm)", String.valueOf(w.getBallTestMm()));
        addRow(table, "Other Reason", w.getOtherReason() != null ? w.getOtherReason() : "—");
        addRow(table, "Time", w.getTime() != null ? w.getTime() : "—");
    }

    private void addAutoclaveData(PdfPTable table, String batchNo) {
        List<AutoclaveCycle> cycles = autoclaveRepo.findByBatchNo(batchNo);

        if (cycles.isEmpty()) {
            addRow(table, "Status", "Not completed");
            return;
        }

        AutoclaveCycle c = cycles.get(0);
        addRow(table, "Autoclave No", c.getAutoclaveNo() != null ? c.getAutoclaveNo() : "—");
        addRow(table, "Run No", c.getRunNo() != null ? c.getRunNo() : "—");
        addRow(table, "Shift", c.getShift() != null ? c.getShift() : "—");
        addRow(table, "Start Date", formatDate(c.getStartedDate()));
        addRow(table, "Started At", c.getStartedAt() != null ? c.getStartedAt() : "—");
        addRow(table, "Completed Date", formatDate(c.getCompletedDate()));
        addRow(table, "Completed At", c.getCompletedAt() != null ? c.getCompletedAt() : "—");
        addRow(table, "Remarks", c.getRemarks() != null ? c.getRemarks() : "—");
    }

    private void addBlockSeparatingData(PdfPTable table, String batchNo) {
        List<BlockSeparating> entries = blockRepo.findByBatchNumber(batchNo);

        if (entries.isEmpty()) {
            addRow(table, "Status", "Not completed");
            return;
        }

        BlockSeparating b = entries.get(0);
        addRow(table, "Date", formatDate(b.getReportDate()));
        addRow(table, "Shift", b.getShift() != null ? b.getShift() : "—");
        addRow(table, "Operator (User ID)", String.valueOf(b.getUserId()));
        addRow(table, "Block Size", b.getBlockSize() != null ? b.getBlockSize() : "—");
        addRow(table, "Time", String.valueOf(b.getTime()));
    }

    private void addCubeTestData(PdfPTable table, String batchNo) {
        List<CubeTestEntity> entries = cubeRepo.findByBatchNo(batchNo);

        if (entries.isEmpty()) {
            addRow(table, "Status", "Not completed");
            return;
        }

        CubeTestEntity c = entries.get(0);
        addRow(table, "Cast Date", formatDate(c.getCastDate()));
        addRow(table, "Testing Date", formatDate(c.getTestingDate()));
        addRow(table, "Shift", c.getShift() != null ? c.getShift() : "—");
        addRow(table, "Operator (User ID)", String.valueOf(c.getUserId()));
        addRow(table, "Cube Dimension", c.getCubeDimensionImmediate() != null ? c.getCubeDimensionImmediate() : "—");
        addRow(table, "Density (kg/m³)", c.getDensityKgM3() != null ? String.valueOf(c.getDensityKgM3()) : "—");
    }

    private void addRejectionData(PdfPTable table, String batchNo) {
        List<RejectionDataEntity> entries = rejectionRepo.findByBatchNo(batchNo);

        if (entries.isEmpty()) {
            addRow(table, "Status", "Not completed");
            return;
        }

        RejectionDataEntity r = entries.get(0);
        addRow(table, "Date", formatDate(r.getDate()));
        addRow(table, "Shift", r.getShift() != null ? r.getShift() : "—");
        addRow(table, "Operator (User ID)", String.valueOf(r.getUserId()));
        addRow(table, "Block Size", r.getBlockSize() != null ? r.getBlockSize() : "—");
        addRow(table, "Qty", r.getQty() != null ? String.valueOf(r.getQty()) : "—");
        addRow(table, "Total Breakages", r.getTotalBreakages() != null ? String.valueOf(r.getTotalBreakages()) : "—");
    }

    // Helpers
    private void addRow(PdfPTable table, String label, String value) {
        PdfPCell labelCell = new PdfPCell(new Phrase(label, normalFont));
        labelCell.setBackgroundColor(new BaseColor(235, 245, 251));
        labelCell.setPadding(5);
        table.addCell(labelCell);

        PdfPCell valueCell = new PdfPCell(new Phrase(value != null ? value : "—", normalFont));
        valueCell.setPadding(5);
        table.addCell(valueCell);
    }

    private String formatDate(Date date) {
        return date != null ? dateFormat.format(date) : "—";
    }

    private String formatStageName(String stage) {
        return stage.replace("_", " ").substring(0, 1).toUpperCase() +
                stage.replace("_", " ").substring(1).toLowerCase();
    }

    // ======================== HORIZONTAL JSON REPORT ========================

    /**
     * Returns a list of rows, one per batch, with all stage data side-by-side.
     * Each row is a Map<String, Object> with nested maps per stage.
     */
    public List<Map<String, Object>> getHorizontalReport(Date fromDate, Date toDate, String batchNo) {
        return getHorizontalReport(fromDate, toDate, batchNo, null);
    }

    public List<Map<String, Object>> getHorizontalReport(Date fromDate, Date toDate, String batchNo, String plantName) {
        List<String> batches;

        if (batchNo != null && !batchNo.isBlank()) {
            batches = List.of(batchNo.trim());
        } else if (fromDate != null && toDate != null) {
            List<ProductionEntry> entries = (plantName != null && !plantName.isBlank())
                    ? productionRepo.findByCreatedDateBetweenAndPlantName(fromDate, toDate, plantName)
                    : productionRepo.findByCreatedDateBetween(fromDate, toDate);
            batches = entries.stream()
                    .map(ProductionEntry::getBatchNo)
                    .filter(Objects::nonNull)
                    .distinct()
                    .sorted()
                    .collect(java.util.stream.Collectors.toList());
        } else {
            List<ProductionEntry> entries = (plantName != null && !plantName.isBlank())
                    ? productionRepo.findByPlantName(plantName)
                    : productionRepo.findAll();
            batches = entries.stream()
                    .map(ProductionEntry::getBatchNo)
                    .filter(Objects::nonNull)
                    .distinct()
                    .sorted()
                    .collect(java.util.stream.Collectors.toList());
        }

        List<Map<String, Object>> result = new ArrayList<>();
        for (String bn : batches) {
            Map<String, Object> row = new LinkedHashMap<>();
            row.put("batchNo", bn);
            row.put("production", buildProductionMap(bn));
            row.put("casting", buildCastingMap(bn));
            row.put("cutting", buildCuttingMap(bn));
            row.put("autoclave", buildAutoclaveMap(bn));
            row.put("blockSeparating", buildBlockSeparatingMap(bn));
            row.put("cubeTest", buildCubeTestMap(bn));
            row.put("rejection", buildRejectionMap(bn));
            result.add(row);
        }
        return result;
    }

    private Map<String, Object> buildProductionMap(String batchNo) {
        Map<String, Object> m = new LinkedHashMap<>();
        List<ProductionEntry> list = productionRepo.findByBatchNo(batchNo);
        if (list.isEmpty())
            return m;
        ProductionEntry p = list.get(0);
        m.put("date", formatDate(p.getCreatedDate()));
        m.put("plantName", nvl(p.getPlantName()));
        m.put("shift", nvl(p.getShift()));
        m.put("siloNo1", nvl(p.getSiloNo1()));
        m.put("literWeight1", nvl(p.getLiterWeight1()));
        m.put("faSolid1", nvl(p.getFaSolid1()));
        m.put("siloNo2", nvl(p.getSiloNo2()));
        m.put("literWeight2", nvl(p.getLiterWeight2()));
        m.put("faSolid2", nvl(p.getFaSolid2()));
        m.put("totalSolid", nvl(p.getTotalSolid()));
        m.put("waterLiter", nvl(p.getWaterLiter()));
        m.put("cementKg", nvl(p.getCementKg()));
        m.put("limeKg", nvl(p.getLimeKg()));
        m.put("gypsumKg", nvl(p.getGypsumKg()));
        m.put("solOilKg", nvl(p.getSolOilKg()));
        m.put("alPowerGm", nvl(p.getAiPowerGm()));
        m.put("tempC", nvl(p.getTempC()));
        m.put("remark", nvl(p.getProductionRemark()));
        m.put("productionTime", nvl(p.getProductionTime()));
        return m;
    }

    private Map<String, Object> buildCastingMap(String batchNo) {
        Map<String, Object> m = new LinkedHashMap<>();
        List<CastingHallReport> list = castingRepo.findByBatchNo(batchNo);
        if (list.isEmpty())
            return m;
        CastingHallReport c = list.get(0);
        m.put("date", formatDate(c.getCreatedDate()));
        m.put("shift", nvl(c.getShift()));
        m.put("size", nvl(c.getSize()));
        m.put("bedNo", nvl(c.getBedNo()));
        m.put("mouldNo", nvl(c.getMouldNo()));
        m.put("castingTime", nvl(c.getCastingTime()));
        m.put("consistency", nvl(c.getConsistency()));
        m.put("flowInCm", nvl(c.getFlowInCm()));
        m.put("tempC", nvl(c.getCastingTempC()));
        m.put("vt", nvl(c.getVt()));
        m.put("massTemp", nvl(c.getMassTemp()));
        m.put("fallingTestMm", nvl(c.getFallingTestMm()));
        m.put("testTime", nvl(c.getTestTime()));
        m.put("hTime", nvl(c.getHTime()));
        m.put("remark", nvl(c.getRemark()));
        return m;
    }

    private Map<String, Object> buildCuttingMap(String batchNo) {
        Map<String, Object> m = new LinkedHashMap<>();
        List<WireCuttingReport> list = cuttingRepo.findByBatchNo(batchNo);
        if (list.isEmpty())
            return m;
        WireCuttingReport w = list.get(0);
        m.put("date", formatDate(w.getCreatedDate()));
        m.put("shift", nvl(w.getShift()));
        m.put("cuttingDate", formatDate(w.getCuttingDate()));
        m.put("mouldNo", nvl(w.getMouldNo()));
        m.put("size", nvl(w.getSize()));
        m.put("ballTestMm", nvl(w.getBallTestMm()));
        m.put("otherReason", nvl(w.getOtherReason()));
        m.put("time", nvl(w.getTime()));
        return m;
    }

    private Map<String, Object> buildAutoclaveMap(String batchNo) {
        Map<String, Object> m = new LinkedHashMap<>();
        List<AutoclaveCycle> list = autoclaveRepo.findByBatchNo(batchNo);
        if (list.isEmpty())
            return m;
        AutoclaveCycle a = list.get(0);
        m.put("autoclaveNo", nvl(a.getAutoclaveNo()));
        m.put("runNo", nvl(a.getRunNo()));
        m.put("shift", nvl(a.getShift()));
        m.put("startDate", formatDate(a.getStartedDate()));
        m.put("startedAt", nvl(a.getStartedAt()));
        m.put("compDate", formatDate(a.getCompletedDate()));
        m.put("completedAt", nvl(a.getCompletedAt()));
        m.put("remarks", nvl(a.getRemarks()));
        return m;
    }

    private Map<String, Object> buildBlockSeparatingMap(String batchNo) {
        Map<String, Object> m = new LinkedHashMap<>();
        List<BlockSeparating> list = blockRepo.findByBatchNumber(batchNo);
        if (list.isEmpty())
            return m;
        BlockSeparating b = list.get(0);
        m.put("date", formatDate(b.getReportDate()));
        m.put("shift", nvl(b.getShift()));
        m.put("blockSize", nvl(b.getBlockSize()));
        m.put("time", nvl(b.getTime()));
        return m;
    }

    private Map<String, Object> buildCubeTestMap(String batchNo) {
        Map<String, Object> m = new LinkedHashMap<>();
        List<CubeTestEntity> list = cubeRepo.findByBatchNo(batchNo);
        if (list.isEmpty())
            return m;
        CubeTestEntity c = list.get(0);
        m.put("castDate", formatDate(c.getCastDate()));
        m.put("testingDate", formatDate(c.getTestingDate()));
        m.put("shift", nvl(c.getShift()));
        m.put("cubeDimension", nvl(c.getCubeDimensionImmediate()));
        m.put("densityKgM3", nvl(c.getDensityKgM3()));
        return m;
    }

    private Map<String, Object> buildRejectionMap(String batchNo) {
        Map<String, Object> m = new LinkedHashMap<>();
        List<RejectionDataEntity> list = rejectionRepo.findByBatchNo(batchNo);
        if (list.isEmpty())
            return m;
        RejectionDataEntity r = list.get(0);
        m.put("date", formatDate(r.getDate()));
        m.put("shift", nvl(r.getShift()));
        m.put("blockSize", nvl(r.getBlockSize()));
        m.put("qty", nvl(r.getQty()));
        m.put("totalBreakages", nvl(r.getTotalBreakages()));
        return m;
    }

    /** Null-safe value formatter */
    private String nvl(Object val) {
        return val != null ? String.valueOf(val) : "—";
    }

    // ======================== HORIZONTAL EXCEL DOWNLOAD ========================

    /**
     * Generates a color-coded Excel workbook with all stages side-by-side.
     * Green=Production, Red=Casting, Blue=Cutting, Purple=Autoclave,
     * Orange=Block Separating, Teal=Cube Test, Pink=Rejection
     */
    public byte[] generateHorizontalExcel(Date fromDate, Date toDate, String batchNo, String upToStage)
            throws IOException {
        return generateHorizontalExcel(fromDate, toDate, batchNo, upToStage, null);
    }

    public byte[] generateHorizontalExcel(Date fromDate, Date toDate, String batchNo, String upToStage,
            String plantName)
            throws IOException {
        List<Map<String, Object>> rows = getHorizontalReport(fromDate, toDate, batchNo, plantName);

        try (XSSFWorkbook wb = new XSSFWorkbook()) {
            org.apache.poi.xssf.usermodel.XSSFSheet sheet = wb.createSheet("Horizontal Report");

            // ── Stage definitions: {key, label, headerRGB, lightRGB, fields[]} ────
            String[][] allStages = {
                    { "production", "PRODUCTION", "2E7D32", "E8F5E9" },
                    { "casting", "CASTING", "C62828", "FFEBEE" },
                    { "cutting", "WIRE CUTTING", "1565C0", "E3F2FD" },
                    { "autoclave", "AUTOCLAVE", "6A1B9A", "F3E5F5" },
                    { "blockSeparating", "BLOCK SEPARATING", "E65100", "FFF3E0" },
                    { "cubeTest", "CUBE TEST", "00695C", "E0F2F1" },
                    { "rejection", "REJECTION", "AD1457", "FCE4EC" }
            };

            int maxCompletedStageIdx = 0;
            if (upToStage != null && !upToStage.isEmpty() && !upToStage.equalsIgnoreCase("CONSOLIDATED")) {
                String normalizedStage = upToStage.toUpperCase().replace(" ", "_");
                if (normalizedStage.contains("WIRE"))
                    normalizedStage = "CUTTING";
                if (normalizedStage.contains("SEPARAT"))
                    normalizedStage = "BLOCK_SEPARATING";
                maxCompletedStageIdx = STAGES.indexOf(normalizedStage);
                if (maxCompletedStageIdx < 0) {
                    maxCompletedStageIdx = STAGES.size() - 1;
                }
            } else {
                for (Map<String, Object> row : rows) {
                    for (int i = 0; i < allStages.length; i++) {
                        String key = allStages[i][0];
                        @SuppressWarnings("unchecked")
                        Map<String, Object> stageData = (Map<String, Object>) row.get(key);
                        if (stageData != null && !stageData.isEmpty()) {
                            if (i > maxCompletedStageIdx) {
                                maxCompletedStageIdx = i;
                            }
                        }
                    }
                }
            }

            String[][] stages = new String[maxCompletedStageIdx + 1][];
            System.arraycopy(allStages, 0, stages, 0, maxCompletedStageIdx + 1);

            // ── Column order per stage ────────────────────────────────────────────
            LinkedHashMap<String, String[]> stageFields = new LinkedHashMap<>();
            stageFields.put("production",
                    new String[] { "date", "shift", "siloNo1", "literWeight1", "faSolid1", "siloNo2", "literWeight2",
                            "faSolid2", "totalSolid", "waterLiter", "cementKg", "limeKg", "gypsumKg", "solOilKg",
                            "alPowerGm", "tempC", "productionTime", "remark" });
            stageFields.put("casting",
                    new String[] { "date", "shift", "size", "bedNo", "mouldNo", "castingTime", "consistency",
                            "flowInCm", "tempC", "vt", "massTemp", "fallingTestMm", "testTime", "hTime", "remark" });
            stageFields.put("cutting", new String[] { "date", "shift", "cuttingDate", "mouldNo", "size", "ballTestMm",
                    "otherReason", "time" });
            stageFields.put("autoclave", new String[] { "autoclaveNo", "runNo", "shift", "startDate", "startedAt",
                    "compDate", "completedAt", "remarks" });
            stageFields.put("blockSeparating", new String[] { "date", "shift", "blockSize", "time" });
            stageFields.put("cubeTest",
                    new String[] { "castDate", "testingDate", "shift", "cubeDimension", "densityKgM3" });
            stageFields.put("rejection", new String[] { "date", "shift", "blockSize", "qty", "totalBreakages" });

            // ── Build CellStyles per stage (header + body) ────────────────────────
            java.util.Map<String, CellStyle> headerStyles = new HashMap<>();
            java.util.Map<String, CellStyle> bodyStyles = new HashMap<>();

            for (String[] stage : stages) {
                String key = stage[0];
                String headerHex = stage[2];
                String bodyHex = stage[3];

                // Header style
                org.apache.poi.xssf.usermodel.XSSFCellStyle hStyle = wb.createCellStyle();
                hStyle.setFillForegroundColor(hexToXSSFColor(wb, headerHex));
                hStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                org.apache.poi.ss.usermodel.Font hFont = wb.createFont();
                hFont.setBold(true);
                hFont.setColor(org.apache.poi.ss.usermodel.IndexedColors.WHITE.getIndex());
                hFont.setFontHeightInPoints((short) 9);
                hStyle.setFont(hFont);
                hStyle.setAlignment(org.apache.poi.ss.usermodel.HorizontalAlignment.CENTER);
                hStyle.setBorderBottom(BorderStyle.THIN);
                hStyle.setBorderRight(BorderStyle.THIN);
                headerStyles.put(key, hStyle);

                // Body style
                org.apache.poi.xssf.usermodel.XSSFCellStyle bStyle = wb.createCellStyle();
                bStyle.setFillForegroundColor(hexToXSSFColor(wb, bodyHex));
                bStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                bStyle.setBorderRight(BorderStyle.THIN);
                bStyle.setAlignment(org.apache.poi.ss.usermodel.HorizontalAlignment.CENTER);
                bodyStyles.put(key, bStyle);
            }

            // Batch column styles
            CellStyle batchHeaderStyle = wb.createCellStyle();
            batchHeaderStyle.setFillForegroundColor(IndexedColors.GREY_50_PERCENT.getIndex());
            batchHeaderStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            org.apache.poi.ss.usermodel.Font bFont = wb.createFont();
            bFont.setBold(true);
            bFont.setColor(IndexedColors.WHITE.getIndex());
            batchHeaderStyle.setFont(bFont);
            batchHeaderStyle.setAlignment(org.apache.poi.ss.usermodel.HorizontalAlignment.CENTER);

            CellStyle batchBodyStyle = wb.createCellStyle();
            org.apache.poi.ss.usermodel.Font bbFont = wb.createFont();
            bbFont.setBold(true);
            batchBodyStyle.setFont(bbFont);
            batchBodyStyle.setAlignment(org.apache.poi.ss.usermodel.HorizontalAlignment.CENTER);
            batchBodyStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            batchBodyStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            // ── ROW 0: Stage banner headers ──────────────────────────────────────
            Row bannerRow = sheet.createRow(0);
            Cell batchBannerCell = bannerRow.createCell(0);
            batchBannerCell.setCellValue("Batch No");
            batchBannerCell.setCellStyle(batchHeaderStyle);

            int col = 1;
            for (String[] stage : stages) {
                String key = stage[0];
                String label = stage[1];
                String[] fields = stageFields.get(key);
                int startCol = col;
                for (int f = 0; f < fields.length; f++) {
                    Cell c = bannerRow.createCell(col + f);
                    if (f == 0)
                        c.setCellValue(label);
                    c.setCellStyle(headerStyles.get(key));
                }
                if (fields.length > 1) {
                    sheet.addMergedRegion(
                            new org.apache.poi.ss.util.CellRangeAddress(0, 0, startCol, startCol + fields.length - 1));
                }
                col += fields.length;
            }

            // ── ROW 1: Column label headers ───────────────────────────────────────
            Row labelRow = sheet.createRow(1);
            Cell batchLabelCell = labelRow.createCell(0);
            batchLabelCell.setCellValue("Batch No");
            batchLabelCell.setCellStyle(batchHeaderStyle);

            col = 1;
            for (String[] stage : stages) {
                String key = stage[0];
                String[] fields = stageFields.get(key);
                for (String field : fields) {
                    Cell c = labelRow.createCell(col++);
                    c.setCellValue(humanize(field));
                    c.setCellStyle(headerStyles.get(key));
                }
            }

            // ── DATA ROWS ─────────────────────────────────────────────────────────
            int rowIdx = 2;
            for (Map<String, Object> row : rows) {
                Row dataRow = sheet.createRow(rowIdx++);

                // Batch No
                Cell batchCell = dataRow.createCell(0);
                batchCell.setCellValue(nvl(row.get("batchNo")));
                batchCell.setCellStyle(batchBodyStyle);

                col = 1;
                for (String[] stage : stages) {
                    String key = stage[0];
                    String[] fields = stageFields.get(key);
                    @SuppressWarnings("unchecked")
                    java.util.Map<String, Object> stageData = (java.util.Map<String, Object>) row.getOrDefault(key,
                            new HashMap<>());
                    CellStyle bStyle = bodyStyles.get(key);
                    for (String field : fields) {
                        Cell c = dataRow.createCell(col++);
                        c.setCellValue(nvl(stageData.get(field)));
                        c.setCellStyle(bStyle);
                    }
                }
            }

            // Auto-size first 60 columns
            for (int i = 0; i < 60; i++)
                sheet.autoSizeColumn(i);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            wb.write(baos);
            return baos.toByteArray();
        }
    }

    private org.apache.poi.xssf.usermodel.XSSFColor hexToXSSFColor(
            org.apache.poi.xssf.usermodel.XSSFWorkbook wb, String hex) {
        byte r = (byte) Integer.parseInt(hex.substring(0, 2), 16);
        byte g = (byte) Integer.parseInt(hex.substring(2, 4), 16);
        byte b = (byte) Integer.parseInt(hex.substring(4, 6), 16);
        return new org.apache.poi.xssf.usermodel.XSSFColor(new byte[] { r, g, b }, null);
    }

    private String humanize(String camelCase) {
        // "fallingTestMm" -> "Falling Test Mm"
        String spaced = camelCase.replaceAll("([A-Z])", " $1");
        return spaced.substring(0, 1).toUpperCase() + spaced.substring(1);
    }

    // ======================== LIFECYCLE BATCH EXPORT ========================

    public byte[] generateBatchLifecycleExcel(String batchNo) throws IOException {
        String query = "SELECT " +
                "p.batch_no AS batch_no, " +
                "p.created_date AS p_date, " +
                "p.shift AS p_shift, " +
                "c.created_date AS c_date, " + // Assuming date field is created_date in CastingHallReport
                "c.size AS c_weight, " + // Will need to verify if weight exists in CastingHallReport
                "cu.cutting_date AS cu_date, " + // In WireCuttingReport it's cutting_date
                "cu.size AS cu_size, " + // In WireCuttingReport it's size
                "a.started_date AS a_date, " + // In AutoclaveCycle it's started_date
                "a.autoclave_no AS a_temp, " + // Mapping to requested placeholders
                "a.run_no AS a_pressure " + // Mapping to requested placeholders
                "FROM production_entry p " +
                "LEFT JOIN casting_hall_report c ON p.batch_no = c.batch_no " +
                "LEFT JOIN wire_cutting_report cu ON p.batch_no = cu.batch_no " +
                "LEFT JOIN autoclave_wagon w ON " +
                "  (CAST(w.e_batch AS CHAR) = p.batch_no OR " +
                "   CAST(w.m_batch AS CHAR) = p.batch_no OR " +
                "   CAST(w.w_batch AS CHAR) = p.batch_no) " +
                "LEFT JOIN autoclave_cycle a ON w.autoclave_id = a.id " +
                "WHERE p.batch_no = ? LIMIT 1";

        List<Map<String, Object>> results = jdbcTemplate.queryForList(query, batchNo);

        try (XSSFWorkbook wb = new XSSFWorkbook()) {
            Sheet sheet = wb.createSheet("Lifecycle - " + batchNo);

            // Header Style
            CellStyle hStyle = wb.createCellStyle();
            hStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            hStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            org.apache.poi.ss.usermodel.Font hFont = wb.createFont();
            hFont.setBold(true);
            hStyle.setFont(hFont);
            hStyle.setAlignment(HorizontalAlignment.CENTER);

            // Create Headers
            String[] headers = {
                    "Batch No", "Production Date", "Production Shift", "Casting Date", "Casting Weight",
                    "Cutting Date", "Cutting Size", "Autoclave Date", "Autoclave Temp", "Autoclave Pressure",
                    "Customer Name", "Sales Invoice No"
            };

            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                Cell c = headerRow.createCell(i);
                c.setCellValue(headers[i]);
                c.setCellStyle(hStyle);
            }

            // Write Data
            if (!results.isEmpty()) {
                Map<String, Object> r = results.get(0);
                Row row = sheet.createRow(1);

                row.createCell(0).setCellValue(nvl(r.get("batch_no")));
                row.createCell(1).setCellValue(formatDateStr(r.get("p_date")));
                row.createCell(2).setCellValue(nvl(r.get("p_shift")));
                row.createCell(3).setCellValue(formatDateStr(r.get("c_date")));
                row.createCell(4).setCellValue("N/A"); // Weight not explicitly in entity but leaving standard
                row.createCell(5).setCellValue(formatDateStr(r.get("cu_date")));
                row.createCell(6).setCellValue(nvl(r.get("cu_size")));
                row.createCell(7).setCellValue(formatDateStr(r.get("a_date")));
                row.createCell(8).setCellValue(nvl(r.get("a_temp")));
                row.createCell(9).setCellValue(nvl(r.get("a_pressure")));
                row.createCell(10).setCellValue("N/A"); // Customer Name
                row.createCell(11).setCellValue("N/A"); // Sales Invoice No
            } else {
                Row row = sheet.createRow(1);
                row.createCell(0).setCellValue(batchNo);
                row.createCell(1).setCellValue("No data found");
            }

            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            wb.write(baos);
            return baos.toByteArray();
        }
    }

    private String formatDateStr(Object obj) {
        if (obj == null)
            return "—";
        if (obj instanceof Date)
            return dateFormat.format((Date) obj);
        if (obj instanceof java.sql.Timestamp)
            return dateFormat.format(new Date(((java.sql.Timestamp) obj).getTime()));
        return String.valueOf(obj);
    }
}

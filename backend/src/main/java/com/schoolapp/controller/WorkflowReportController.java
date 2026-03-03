package com.schoolapp.controller;

import com.schoolapp.service.WorkflowReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/workflow")
@CrossOrigin(origins = "*")
public class WorkflowReportController {

    @Autowired
    private WorkflowReportService reportService;

    /**
     * GET /api/workflow/report/{batchNo}?upToStage=CUTTING
     * Downloads combined PDF report for a batch up to the specified stage
     */
    @GetMapping("/report/{batchNo}")
    public ResponseEntity<byte[]> downloadReport(
            @PathVariable String batchNo,
            @RequestParam(defaultValue = "REJECTION") String upToStage) {

        try {
            byte[] pdf = reportService.generateReport(batchNo, upToStage);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment",
                    "workflow_report_" + batchNo + ".pdf");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(pdf);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * GET /api/workflow/report/horizontal
     * Returns JSON list of batch rows with all stage data combined horizontally.
     * Optional params: fromDate (yyyy-MM-dd), toDate (yyyy-MM-dd), batchNo
     */
    @GetMapping("/report/horizontal")
    public ResponseEntity<List<Map<String, Object>>> getHorizontalReport(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date fromDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date toDate,
            @RequestParam(required = false) String batchNo,
            @RequestParam(required = false) String plantName) {

        try {
            List<Map<String, Object>> rows = reportService.getHorizontalReport(fromDate, toDate, batchNo, plantName);
            return ResponseEntity.ok(rows);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * GET /api/workflow/report/horizontal/download?format=excel&batchNo=B001
     * Downloads the combined horizontal report as an Excel file.
     */
    @GetMapping("/report/horizontal/download")
    public ResponseEntity<byte[]> downloadHorizontalReport(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date fromDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date toDate,
            @RequestParam(required = false) String batchNo,
            @RequestParam(required = false) String upToStage,
            @RequestParam(required = false) String plantName,
            @RequestParam(defaultValue = "excel") String format) {

        try {
            byte[] data = reportService.generateHorizontalExcel(fromDate, toDate, batchNo, upToStage, plantName);

            String filename = batchNo != null && !batchNo.isBlank()
                    ? "horizontal_report_" + batchNo + ".xlsx"
                    : "horizontal_report.xlsx";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType(
                    "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
            headers.setContentDispositionFormData("attachment", filename);

            return ResponseEntity.ok().headers(headers).body(data);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/report/lifecycle/download/{batchNo}")
    public ResponseEntity<byte[]> downloadLifecycleReport(@PathVariable String batchNo) {
        try {
            byte[] data = reportService.generateBatchLifecycleExcel(batchNo);

            String filename = "lifecycle_report_" + batchNo + ".xlsx";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType(
                    "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
            headers.setContentDispositionFormData("attachment", filename);

            return ResponseEntity.ok().headers(headers).body(data);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
}

package com.schoolapp.controller;

import com.schoolapp.service.WorkflowReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reports")
@CrossOrigin(origins = "*")
public class ReportController {

    @Autowired
    private WorkflowReportService reportService;

    /**
     * GET /api/reports/{batchId}/{stageName}
     * Downloads combined PDF report for a batch up to the specified stage
     */
    @GetMapping("/{batchId}/{stageName}")
    public ResponseEntity<byte[]> downloadReport(
            @PathVariable String batchId,
            @PathVariable String stageName,
            @RequestParam(defaultValue = "pdf") String format) {

        try {
            byte[] fileContent;
            String contentType;
            String fileName;

            if ("excel".equalsIgnoreCase(format) || "xlsx".equalsIgnoreCase(format)) {
                fileContent = reportService.generateExcelReport(batchId, stageName);
                contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
                fileName = "workflow_report_" + batchId + "_" + stageName + ".xlsx";
            } else {
                fileContent = reportService.generateReport(batchId, stageName);
                contentType = MediaType.APPLICATION_PDF_VALUE;
                fileName = "workflow_report_" + batchId + "_" + stageName + ".pdf";
            }

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType(contentType));
            headers.setContentDispositionFormData("attachment", fileName);

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(fileContent);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
}

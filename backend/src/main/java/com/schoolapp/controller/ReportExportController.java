package com.schoolapp.controller;

import com.schoolapp.service.WorkflowReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/api/reports")
@CrossOrigin("*")
public class ReportExportController {

    @Autowired
    private WorkflowReportService workflowReportService;

    @GetMapping("/{stage}/export")
    public ResponseEntity<byte[]> exportReport(
            @PathVariable String stage,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date fromDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date toDate,
            @RequestParam String format) {

        byte[] fileContent;
        String fileName;
        String contentType;

        if ("excel".equalsIgnoreCase(format) || "xlsx".equalsIgnoreCase(format)) {
            try {
                fileContent = workflowReportService.generateFlatWorkflowExcelReport(fromDate, toDate, stage);
                fileName = (stage.equalsIgnoreCase("CONSOLIDATED") ? "Consolidated_Workflow" : stage) + "_Report.xlsx";
                contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.internalServerError().build();
            }
        } else {
            try {
                fileContent = workflowReportService.generateFlatWorkflowPdfReport(fromDate, toDate, stage);
                fileName = (stage.equalsIgnoreCase("CONSOLIDATED") ? "Consolidated_Workflow" : stage) + "_Report.pdf";
                contentType = MediaType.APPLICATION_PDF_VALUE;
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.internalServerError().build();
            }
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
                .contentType(MediaType.parseMediaType(contentType))
                .body(fileContent);
    }
}

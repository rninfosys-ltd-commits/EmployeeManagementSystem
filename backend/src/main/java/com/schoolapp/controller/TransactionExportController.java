package com.schoolapp.controller;

import com.schoolapp.dto.TransactionExportRequest;
import com.schoolapp.service.TransactionReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/api/transactions")
@CrossOrigin("*")
public class TransactionExportController {

    @Autowired
    private TransactionReportService exportService;

    @Autowired
    private com.schoolapp.repository.CustomerRepo customerRepo;

    @GetMapping("/parties")
    public ResponseEntity<java.util.List<com.schoolapp.entity.Customer>> getParties() {
        return ResponseEntity.ok(customerRepo.findAll());
    }

    @GetMapping("/export")
    public ResponseEntity<byte[]> exportTransactions(
            @RequestParam String type,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date fromDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date toDate,
            @RequestParam(required = false) String partyName,
            @RequestParam(required = false) Double minAmount,
            @RequestParam(required = false) Double maxAmount,
            @RequestParam String format) {

        try {
            TransactionExportRequest request = new TransactionExportRequest();
            request.setType(type);
            request.setFromDate(fromDate);
            request.setToDate(toDate);
            request.setPartyName(partyName);
            request.setMinAmount(minAmount);
            request.setMaxAmount(maxAmount);
            request.setFormat(format);

            byte[] fileContent = exportService.exportTransactions(request);
            String fileName = type.toLowerCase() + "_transactions_" + System.currentTimeMillis() +
                    ("excel".equalsIgnoreCase(format) ? ".xlsx" : ".pdf");

            String contentType = "excel".equalsIgnoreCase(format)
                    ? "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
                    : MediaType.APPLICATION_PDF_VALUE;

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
                    .contentType(MediaType.parseMediaType(contentType))
                    .body(fileContent);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
}

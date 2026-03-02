package com.schoolapp.controller;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.schoolapp.entity.Receipt;
import com.schoolapp.entity.UserEntity;
import com.schoolapp.service.ReceiptService;
import com.schoolapp.service.UsersService;

import java.util.Base64;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/receipts")

public class ReceiptController {

    private final ReceiptService service;
    private final UsersService usersService;

    public ReceiptController(ReceiptService service, UsersService usersService) {
        this.service = service;
        this.usersService = usersService;
    }

    @GetMapping
    public List<Receipt> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public Receipt getById(@PathVariable Long id) {
        return service.getById(id);
    }

    // ================= CREATE =================
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Receipt create(
            @RequestParam(required = false) Long partyId,
            @RequestParam(required = false) String mobile,
            @RequestParam Integer transactionType,
            @RequestParam Integer paymentMode,
            @RequestParam(required = false) String transactionId,
            @RequestParam Double amount,
            @RequestParam String receiptDate,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) MultipartFile receiptImage,
            @RequestParam Long createdBy) throws Exception {

        Receipt r = new Receipt();

        r.setCustomerId(partyId);
        r.setPartyId(partyId);

        // ✅ Fetch from UserEntity table
        if (partyId != null) {
            UserEntity u = usersService.getUser(partyId);
            if (u != null) {
                r.setPartyName(u.getUsername());
            }
        }

        r.setMobile(mobile);
        r.setTransactionType(transactionType);
        r.setPaymentMode(paymentMode);
        r.setTransactionId(transactionId);
        r.setAmount(amount);
        r.setReceiptDate(receiptDate);
        r.setDescription(description);
        r.setCreatedBy(createdBy);

        if (receiptImage != null && !receiptImage.isEmpty()) {
            r.setReceiptImage(Base64.getEncoder().encodeToString(receiptImage.getBytes()));
        }

        return service.saveReceipt(r);
    }

    // ================= UPDATE =================
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Receipt update(
            @PathVariable Long id,
            @RequestParam(required = false) Long partyId,
            @RequestParam(required = false) String mobile,
            @RequestParam Integer transactionType,
            @RequestParam Integer paymentMode,
            @RequestParam(required = false) String transactionId,
            @RequestParam Double amount,
            @RequestParam String receiptDate,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) MultipartFile receiptImage) throws Exception {

        Receipt existing = service.getById(id);

        existing.setCustomerId(partyId);
        existing.setPartyId(partyId);

        if (partyId != null) {
            UserEntity u = usersService.getUser(partyId);
            if (u != null) {
                existing.setPartyName(u.getUsername());
            }
        }

        existing.setMobile(mobile);
        existing.setTransactionType(transactionType);
        existing.setPaymentMode(paymentMode);
        existing.setTransactionId(transactionId);
        existing.setAmount(amount);
        existing.setReceiptDate(receiptDate);
        existing.setDescription(description);

        if (receiptImage != null && !receiptImage.isEmpty()) {
            existing.setReceiptImage(Base64.getEncoder().encodeToString(receiptImage.getBytes()));
        }

        return service.saveReceipt(existing);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok("Deleted");
    }

    @GetMapping("/ledger/{customerId}")
    public Map<String, Object> ledger(
            @PathVariable Long customerId,
            @RequestParam(required = false) Long loggedInUserId) {
        return service.getLedger(customerId, loggedInUserId);
    }
}

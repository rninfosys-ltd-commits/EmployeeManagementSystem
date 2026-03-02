package com.schoolapp.service;

import org.springframework.stereotype.Service;

import com.schoolapp.entity.Receipt;
import com.schoolapp.entity.UserEntity;
import com.schoolapp.repository.ReceiptRepository;
import com.schoolapp.repository.UserRepository;

import java.util.*;

@Service
public class ReceiptService {

    private final ReceiptRepository repo;
    private final UserRepository userRepo;

    public ReceiptService(ReceiptRepository repo, UserRepository userRepo) {
        this.repo = repo;
        this.userRepo = userRepo;
    }

    public List<Receipt> getAll() { 
        return repo.findAll(); 
    }

    public Receipt getById(Long id) { 
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Receipt not found")); 
    }

    public Receipt saveReceipt(Receipt r) { 
        return repo.save(r); 
    }

    public void delete(Long id) { 
        repo.deleteById(id); 
    }

    public Map<String, Object> getLedger(Long customerId, Long loggedInUserId) {

        List<Receipt> list = (customerId == null) 
                ? Collections.emptyList() 
                : repo.findByCustomerId(customerId);

        list.sort(Comparator.comparing(Receipt::getReceiptDate));

        double balance = 0.0;
        List<Map<String, Object>> ledgerRows = new ArrayList<>();
        int idx = 1;

        for (Receipt r : list) {

            if (r.getTransactionType() != null && r.getTransactionType() == 1)
                balance += r.getAmount();
            else
                balance -= r.getAmount();

            Map<String, Object> row = new HashMap<>();
            row.put("srNo", idx++);
            row.put("receiptDate", r.getReceiptDate());
            row.put("transactionId", r.getTransactionId());
            row.put("transactionTypeLabel", r.getTransactionType() == 1 ? "Receipt" : "Payment");
            row.put("paymentModeLabel", getPaymentModeLabel(r.getPaymentMode()));
            row.put("amount", r.getAmount());
            row.put("closingBalance", balance);

            ledgerRows.add(row);
        }

        // ================= CUSTOMER INFO =================
        Map<String, Object> customerInfo = new HashMap<>();

        if (customerId != null) {

            Optional<UserEntity> cu = userRepo.findById(customerId);

            if (cu.isPresent()) {
                UserEntity u = cu.get();

                customerInfo.put("id", u.getId());
                customerInfo.put("name", u.getUsername());
                customerInfo.put("email", u.getEmail());
            } else {
                customerInfo.put("id", customerId);
                customerInfo.put("name", "Customer " + customerId);
                customerInfo.put("email", "");
            }
        }

        // ================= LOGGED IN USER =================
        Map<String, Object> loggedInUserInfo = new HashMap<>();

        if (loggedInUserId != null) {

            Optional<UserEntity> lu = userRepo.findById(loggedInUserId);

            lu.ifPresent(u -> {
                loggedInUserInfo.put("id", u.getId());
                loggedInUserInfo.put("name", u.getUsername());
                loggedInUserInfo.put("email", u.getEmail());
            });
        }

        Map<String, Object> resp = new HashMap<>();
        resp.put("customerInfo", customerInfo);
        resp.put("ledger", ledgerRows);
        resp.put("loggedInUser", loggedInUserInfo);

        return resp;
    }

    private String getPaymentModeLabel(Integer m) {
        if (m == null) return "-";

        return switch (m) {
            case 0 -> "Cash";
            case 1 -> "UPI";
            case 2 -> "Bank";
            case 3 -> "Card";
            default -> "-";
        };
    }
}

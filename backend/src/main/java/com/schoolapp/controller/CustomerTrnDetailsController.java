package com.schoolapp.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.schoolapp.entity.CustomerTrnDetails;
import com.schoolapp.service.BatchDetailsService;

@RestController
@RequestMapping("/api/customer-trn")
public class CustomerTrnDetailsController {

    @Autowired
    private BatchDetailsService batchService;

    @GetMapping("/batch/{bactno}")
    public List<CustomerTrnDetails> getTransactionsByBatch(@PathVariable Long bactno) {
        return batchService.getTransactions(bactno);
    }

    @PostMapping("/{bactno}")
    public CustomerTrnDetails addTransaction(@PathVariable Long bactno, @RequestBody CustomerTrnDetails trn) {
        return batchService.addTransaction(bactno, trn);
    }
}

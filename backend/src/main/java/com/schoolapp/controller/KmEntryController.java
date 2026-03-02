package com.schoolapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.schoolapp.entity.KmDetails;
import com.schoolapp.service.KmBatchService;
import com.schoolapp.service.KmService;

import java.util.List;

@RestController
@RequestMapping("/api/km-entry")

public class KmEntryController {

    @Autowired
    private KmBatchService kmBatchService;

    @Autowired
    private KmService kmService;

    @PostMapping("/{batchNo}")
    public KmDetails addEntry(@PathVariable Long batchNo, @RequestBody KmDetails entry) {
        return kmBatchService.addEntry(batchNo, entry);
    }

    @GetMapping("/batch/{batchNo}")
    public List<KmDetails> getEntries(@PathVariable Long batchNo) {
        return kmBatchService.getEntries(batchNo);
    }

    @PutMapping("/{id}")
    public KmDetails update(@PathVariable Long id, @RequestBody KmDetails entry) {
        return kmService.update(id, entry);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        kmService.delete(id);
    }
}

package com.schoolapp.service.export.impl;

import com.schoolapp.entity.BlockSeparating;
import com.schoolapp.repository.BlockSeparatingRepository;
import com.schoolapp.service.export.StageReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class BlockSeparatingExportServiceImpl implements StageReportService {

    @Autowired
    private BlockSeparatingRepository repository;

    @Override
    public String getStageName() {
        return "BLOCK_SEPARATING";
    }

    @Override
    public String[] getHeaders() {
        return new String[] { "Batch No", "Casting Date", "Block Size", "Time", "Shift", "Report Date" };
    }

    @Override
    public List<Map<String, Object>> getData(Date fromDate, Date toDate) {
        List<BlockSeparating> reports = repository.findByReportDateBetween(fromDate, toDate);
        return reports.stream().map(r -> {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("Batch No", r.getBatchNumber());
            map.put("Casting Date", r.getCastingDate());
            map.put("Block Size", r.getBlockSize());
            map.put("Time", r.getTime());
            map.put("Shift", r.getShift());
            map.put("Report Date", r.getReportDate());
            return map;
        }).collect(Collectors.toList());
    }
}

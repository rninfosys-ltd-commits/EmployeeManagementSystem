package com.schoolapp.service.export.impl;

import com.schoolapp.entity.WireCuttingReport;
import com.schoolapp.repository.WireCuttingReportRepository;
import com.schoolapp.service.export.StageReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class WireCuttingExportServiceImpl implements StageReportService {

    @Autowired
    private WireCuttingReportRepository repository;

    @Override
    public String getStageName() {
        return "WIRE_CUTTING";
    }

    @Override
    public String[] getHeaders() {
        return new String[] { "Batch No", "Cutting Date", "Mould No", "Size", "Ball Test (mm)", "Time", "Shift",
                "Created Date" };
    }

    @Override
    public List<Map<String, Object>> getData(Date fromDate, Date toDate) {
        List<WireCuttingReport> reports = repository.findByCreatedDateBetween(fromDate, toDate);
        return reports.stream().map(r -> {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("Batch No", r.getBatchNo());
            map.put("Cutting Date", r.getCuttingDate());
            map.put("Mould No", r.getMouldNo());
            map.put("Size", r.getSize());
            map.put("Ball Test (mm)", r.getBallTestMm());
            map.put("Time", r.getTime());
            map.put("Shift", r.getShift());
            map.put("Created Date", r.getCreatedDate());
            return map;
        }).collect(Collectors.toList());
    }
}

package com.schoolapp.service.export.impl;

import com.schoolapp.entity.CastingHallReport;
import com.schoolapp.repository.CastingHallReportRepository;
import com.schoolapp.service.export.StageReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CastingExportServiceImpl implements StageReportService {

    @Autowired
    private CastingHallReportRepository repository;

    @Override
    public String getStageName() {
        return "CASTING";
    }

    @Override
    public String[] getHeaders() {
        return new String[] { "Batch No", "Size", "Bed No", "Mould No", "Casting Time", "Consistency", "Flow (cm)",
                "Temp (C)", "VT", "Mass Temp", "Falling Test", "Test Time", "H Time", "Shift", "Date" };
    }

    @Override
    public List<Map<String, Object>> getData(Date fromDate, Date toDate) {
        List<CastingHallReport> reports = repository.findByCreatedDateBetween(fromDate, toDate);
        return reports.stream().map(r -> {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("Batch No", r.getBatchNo());
            map.put("Size", r.getSize());
            map.put("Bed No", r.getBedNo());
            map.put("Mould No", r.getMouldNo());
            map.put("Casting Time", r.getCastingTime());
            map.put("Consistency", r.getConsistency());
            map.put("Flow (cm)", r.getFlowInCm());
            map.put("Temp (C)", r.getCastingTempC());
            map.put("VT", r.getVt());
            map.put("Mass Temp", r.getMassTemp());
            map.put("Falling Test", r.getFallingTestMm());
            map.put("Test Time", r.getTestTime());
            map.put("H Time", r.getHTime());
            map.put("Shift", r.getShift());
            map.put("Date", r.getCreatedDate());
            return map;
        }).collect(Collectors.toList());
    }
}

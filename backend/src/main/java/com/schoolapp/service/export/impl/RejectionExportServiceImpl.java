package com.schoolapp.service.export.impl;

import com.schoolapp.entity.RejectionDataEntity;
import com.schoolapp.repository.RejectionDataRepository;
import com.schoolapp.service.export.StageReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RejectionExportServiceImpl implements StageReportService {

    @Autowired
    private RejectionDataRepository repository;

    @Override
    public String getStageName() {
        return "REJECTION";
    }

    @Override
    public String[] getHeaders() {
        return new String[] { "Batch No", "Date", "Block Size", "Qty", "Shift", "Corner Damage", "Eruption", "Top Side",
                "Side Crack", "Rising Crack", "Centre Crack", "Total Breakages" };
    }

    @Override
    public List<Map<String, Object>> getData(Date fromDate, Date toDate) {
        List<RejectionDataEntity> reports = repository.findByDateBetween(fromDate, toDate);
        return reports.stream().map(r -> {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("Batch No", r.getBatchNo());
            map.put("Date", r.getDate());
            map.put("Block Size", r.getBlockSize());
            map.put("Qty", r.getQty());
            map.put("Shift", r.getShift());
            map.put("Corner Damage", r.getCornerDamage());
            map.put("Eruption", r.getEruptionType());
            map.put("Top Side", r.getTopSideDamages());
            map.put("Side Crack", r.getSideCrackThermalCrack());
            map.put("Rising Crack", r.getRisingCrack());
            map.put("Centre Crack", r.getCentreCrack());
            map.put("Total Breakages", r.getTotalBreakages());
            return map;
        }).collect(Collectors.toList());
    }
}

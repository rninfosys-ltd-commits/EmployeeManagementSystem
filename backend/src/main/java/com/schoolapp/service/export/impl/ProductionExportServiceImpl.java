package com.schoolapp.service.export.impl;

import com.schoolapp.entity.ProductionEntry;
import com.schoolapp.repository.ProductionEntryRepository;
import com.schoolapp.service.export.StageReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductionExportServiceImpl implements StageReportService {

    @Autowired
    private ProductionEntryRepository repository;

    @Override
    public String getStageName() {
        return "PRODUCTION";
    }

    @Override
    public String[] getHeaders() {
        return new String[] {
                "Batch No", "Shift", "Date", "Silo 1", "Liter Wt 1", "FA Solid 1",
                "Silo 2", "Liter Wt 2", "FA Solid 2", "Total Solid",
                "Water (L)", "Cement (kg)", "Lime (kg)", "Gypsum (kg)", "Sol Oil (kg)", "Al Power (gm)",
                "Production Time", "Remark"
        };
    }

    @Override
    public List<Map<String, Object>> getData(Date fromDate, Date toDate) {
        List<ProductionEntry> entries = repository.findByCreatedDateBetween(fromDate, toDate);
        return entries.stream().map(e -> {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("Batch No", e.getBatchNo());
            map.put("Shift", e.getShift());
            map.put("Date", e.getCreatedDate());
            map.put("Silo 1", e.getSiloNo1());
            map.put("Liter Wt 1", e.getLiterWeight1());
            map.put("FA Solid 1", e.getFaSolid1());
            map.put("Silo 2", e.getSiloNo2());
            map.put("Liter Wt 2", e.getLiterWeight2());
            map.put("FA Solid 2", e.getFaSolid2());
            map.put("Total Solid", e.getTotalSolid());
            map.put("Water (L)", e.getWaterLiter());
            map.put("Cement (kg)", e.getCementKg());
            map.put("Lime (kg)", e.getLimeKg());
            map.put("Gypsum (kg)", e.getGypsumKg());
            map.put("Sol Oil (kg)", e.getSolOilKg());
            map.put("Al Power (gm)", e.getAiPowerGm());
            map.put("Production Time", e.getProductionTime());
            map.put("Remark", e.getProductionRemark());
            return map;
        }).collect(Collectors.toList());
    }
}

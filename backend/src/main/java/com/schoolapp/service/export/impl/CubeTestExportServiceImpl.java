package com.schoolapp.service.export.impl;

import com.schoolapp.entity.CubeTestEntity;
import com.schoolapp.repository.CubeTestRepository;
import com.schoolapp.service.export.StageReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CubeTestExportServiceImpl implements StageReportService {

    @Autowired
    private CubeTestRepository repository;

    @Override
    public String getStageName() {
        return "CUBE_TEST";
    }

    @Override
    public String[] getHeaders() {
        return new String[] { "Batch No", "Cast Date", "Testing Date", "Shift", "Dimension (Imm)", "Density",
                "Comp Strength (OD)", "Comp Strength (M)" };
    }

    @Override
    public List<Map<String, Object>> getData(Date fromDate, Date toDate) {
        List<CubeTestEntity> reports = repository.findByTestingDateBetween(fromDate, toDate);
        return reports.stream().map(r -> {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("Batch No", r.getBatchNo());
            map.put("Cast Date", r.getCastDate());
            map.put("Testing Date", r.getTestingDate());
            map.put("Shift", r.getShift());
            map.put("Dimension (Imm)", r.getCubeDimensionImmediate());
            map.put("Density", r.getDensityKgM3());
            map.put("Comp Strength (OD)", r.getCompStrengthOverDry());
            map.put("Comp Strength (M)", r.getCompStrengthMoisture());
            return map;
        }).collect(Collectors.toList());
    }
}

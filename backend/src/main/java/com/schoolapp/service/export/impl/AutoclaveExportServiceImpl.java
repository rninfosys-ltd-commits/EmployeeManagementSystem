package com.schoolapp.service.export.impl;

import com.schoolapp.entity.AutoclaveCycle;
import com.schoolapp.repository.AutoclaveRepository;
import com.schoolapp.service.export.StageReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AutoclaveExportServiceImpl implements StageReportService {

    @Autowired
    private AutoclaveRepository repository;

    @Override
    public String getStageName() {
        return "AUTOCLAVE";
    }

    @Override
    public String[] getHeaders() {
        return new String[] { "Autoclave No", "Run No", "Started At", "Started Date", "Completed At", "Completed Date",
                "Shift", "Remarks" };
    }

    @Override
    public List<Map<String, Object>> getData(Date fromDate, Date toDate) {
        List<AutoclaveCycle> reports = repository.findByStartedDateBetween(fromDate, toDate);
        return reports.stream().map(r -> {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("Autoclave No", r.getAutoclaveNo());
            map.put("Run No", r.getRunNo());
            map.put("Started At", r.getStartedAt());
            map.put("Started Date", r.getStartedDate());
            map.put("Completed At", r.getCompletedAt());
            map.put("Completed Date", r.getCompletedDate());
            map.put("Shift", r.getShift());
            map.put("Remarks", r.getRemarks());
            return map;
        }).collect(Collectors.toList());
    }
}

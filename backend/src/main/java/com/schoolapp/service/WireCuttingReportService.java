package com.schoolapp.service;

import java.util.List;

import com.schoolapp.dao.WireCuttingImportRequestDto;
import com.schoolapp.dao.WireCuttingImportResponseDto;
import com.schoolapp.dao.WireCuttingReportRequestDto;
import com.schoolapp.entity.WireCuttingReport;

public interface WireCuttingReportService {

    WireCuttingReport save(WireCuttingReportRequestDto dto);

    WireCuttingReport update(Long id, WireCuttingReportRequestDto dto);

    void delete(Long id);

    List<WireCuttingReport> getAll();

    // 🔥 UPDATED
    WireCuttingReport approve(Long id, Long userId, String role, String username);

    // 🔥 UPDATED
    WireCuttingReport reject(Long id, Long userId, String role, String username, String reason);

    WireCuttingImportResponseDto importWireCutting(WireCuttingImportRequestDto dto);
}

package com.schoolapp.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.schoolapp.dao.CastingHallReportRequestDto;
import com.schoolapp.dao.CastingImportRequestDto;
import com.schoolapp.dao.CastingImportResponse;
import com.schoolapp.entity.CastingHallReport;

public interface CastingHallReportService {

	CastingHallReport save(CastingHallReportRequestDto dto);

	CastingHallReport update(Long id, CastingHallReportRequestDto dto);

	void delete(Long id);

	Page<CastingHallReport> getAll(Pageable pageable, String plantName);

	CastingHallReport approve(Long id, Long userId, String role);

	CastingHallReport reject(Long id, Long userId, String role, String reason);

	CastingHallReport getById(Long id);

	CastingImportResponse importCasting(CastingImportRequestDto dto);

}

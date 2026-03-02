package com.schoolapp.repository;

//package com.Crmemp.repository;

//import com.Crmemp.entity.CastingHallReport;
import org.springframework.data.jpa.repository.JpaRepository;

import com.schoolapp.entity.CastingHallReport;

import java.util.Date;
import java.util.List;

public interface CastingHallReportRepository
                extends JpaRepository<CastingHallReport, Long> {
        boolean existsByBatchNo(String batchNo);

        List<CastingHallReport> findByCreatedDateBetween(Date start, Date end);

        List<CastingHallReport> findByBatchNo(String batchNo);
}

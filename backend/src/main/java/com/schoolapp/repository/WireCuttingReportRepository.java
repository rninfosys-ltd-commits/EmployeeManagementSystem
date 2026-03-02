package com.schoolapp.repository;

//package com.Crmemp.repository;

//import com.Crmemp.entity.WireCuttingReport;
import org.springframework.data.jpa.repository.JpaRepository;

import com.schoolapp.entity.WireCuttingReport;

import java.util.Date;
import java.util.List;

public interface WireCuttingReportRepository
                extends JpaRepository<WireCuttingReport, Long> {
        boolean existsByBatchNo(String batchNo);

        List<WireCuttingReport> findByCreatedDateBetween(Date start, Date end);

        List<WireCuttingReport> findByBatchNo(String batchNo);
}

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

        @org.springframework.data.jpa.repository.Query("SELECT w FROM WireCuttingReport w WHERE w.plantName = :plantName")
        List<WireCuttingReport> findByPlantName(
                        @org.springframework.data.repository.query.Param("plantName") String plantName);

        @org.springframework.data.jpa.repository.Query("SELECT w FROM WireCuttingReport w WHERE w.createdDate BETWEEN :start AND :end AND w.plantName = :plantName")
        List<WireCuttingReport> findByCreatedDateBetweenAndPlantName(
                        @org.springframework.data.repository.query.Param("start") Date start,
                        @org.springframework.data.repository.query.Param("end") Date end,
                        @org.springframework.data.repository.query.Param("plantName") String plantName);
}

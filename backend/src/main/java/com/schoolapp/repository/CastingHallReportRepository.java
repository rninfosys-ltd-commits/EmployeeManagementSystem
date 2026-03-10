package com.schoolapp.repository;

//package com.Crmemp.repository;

//import com.Crmemp.entity.CastingHallReport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.schoolapp.entity.CastingHallReport;

import java.util.Date;
import java.util.List;

public interface CastingHallReportRepository
                extends JpaRepository<CastingHallReport, Long> {
        boolean existsByBatchNo(String batchNo);

        List<CastingHallReport> findByBatchNo(String batchNo);

        Page<CastingHallReport> findByPlantNameContainingIgnoreCase(String plantName, Pageable pageable);

        List<CastingHallReport> findByCreatedDateBetween(Date start, Date end);

        @org.springframework.data.jpa.repository.Query("SELECT c FROM CastingHallReport c WHERE c.plantName = :plantName")
        List<CastingHallReport> findByPlantName(
                        @org.springframework.data.repository.query.Param("plantName") String plantName);

        @org.springframework.data.jpa.repository.Query("SELECT c FROM CastingHallReport c WHERE c.createdDate BETWEEN :start AND :end AND c.plantName = :plantName")
        List<CastingHallReport> findByCreatedDateBetweenAndPlantName(
                        @org.springframework.data.repository.query.Param("start") Date start,
                        @org.springframework.data.repository.query.Param("end") Date end,
                        @org.springframework.data.repository.query.Param("plantName") String plantName);
}

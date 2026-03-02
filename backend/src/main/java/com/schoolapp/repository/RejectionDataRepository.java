package com.schoolapp.repository;

//package com.Crmemp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.schoolapp.entity.RejectionDataEntity;
//import com.Crmemp.entity.RejectionDataEntity;

import java.util.Date;
import java.util.List;

public interface RejectionDataRepository extends JpaRepository<RejectionDataEntity, Long> {
    boolean existsByBatchNo(String batchNo);

    List<RejectionDataEntity> findByDateBetween(Date start, Date end);

    List<RejectionDataEntity> findByBatchNo(String batchNo);
}

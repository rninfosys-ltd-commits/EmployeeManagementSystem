package com.schoolapp.repository;

//package com.Crmemp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.schoolapp.entity.CubeTestEntity;

//import com.Crmemp.entity.CubeTestEntity;

import java.util.Date;
import java.util.List;

public interface CubeTestRepository extends JpaRepository<CubeTestEntity, Long> {
    boolean existsByBatchNo(String batchNo);

    List<CubeTestEntity> findByTestingDateBetween(Date start, Date end);

    List<CubeTestEntity> findByBatchNo(String batchNo);
}

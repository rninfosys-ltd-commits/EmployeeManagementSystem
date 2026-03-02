package com.schoolapp.repository;

//import com.employeemanagement.entity.KmDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import com.schoolapp.entity.KmDetails;

//import com.Crmemp.entity.KmDetails;

import java.util.List;

public interface KmRepository extends JpaRepository<KmDetails, Long> {

    List<KmDetails> findByBatch_KmBatchNo(Long batchNo);
}

package com.schoolapp.repository;

//import com.employeemanagement.entity.KmBatch;
import org.springframework.data.jpa.repository.JpaRepository;

import com.schoolapp.entity.KmBatch;

//import com.Crmemp.entity.KmBatch;

public interface KmBatchRepository extends JpaRepository<KmBatch, Long> {
}

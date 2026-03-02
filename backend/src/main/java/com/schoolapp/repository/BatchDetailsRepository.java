package com.schoolapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.schoolapp.entity.BatchDetails;

//import com.Crmemp.entity.BatchDetails;

//import com.employeemanagement.entity.BatchDetails;

@Repository
public interface BatchDetailsRepository extends JpaRepository<BatchDetails, Long> {
}

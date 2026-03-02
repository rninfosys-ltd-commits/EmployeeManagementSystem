package com.schoolapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.schoolapp.entity.Taluka;

//import com.Crmemp.entity.Taluka;

//import com.employeemanagement.entity.Taluka;
import java.util.List;

public interface TalukaRepository extends JpaRepository<Taluka, Long> {
    List<Taluka> findByDistrictId(Long districtId);
}

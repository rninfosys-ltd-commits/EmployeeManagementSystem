package com.schoolapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.schoolapp.entity.City;

//import com.Crmemp.entity.City;

//import com.employeemanagement.entity.City;
import java.util.List;

public interface CityRepository extends JpaRepository<City, Long> {
    List<City> findByTalukaId(Long talukaId);
}

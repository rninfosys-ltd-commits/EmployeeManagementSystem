package com.schoolapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.schoolapp.entity.District;

@Repository
public interface DistrictRepo extends JpaRepository<District, Integer> {

}

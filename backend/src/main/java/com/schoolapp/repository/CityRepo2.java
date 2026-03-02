package com.schoolapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.schoolapp.entity.City2;

@Repository
public interface CityRepo2 extends JpaRepository<City2, Integer> {

}

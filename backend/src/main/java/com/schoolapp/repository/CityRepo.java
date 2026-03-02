package com.schoolapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.schoolapp.entity.City;

@Repository
public interface CityRepo extends JpaRepository<City, Integer>{

}

package com.schoolapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.schoolapp.entity.Company;

@Repository
public interface CompanyRepo extends JpaRepository<Company, Integer> {
	

}


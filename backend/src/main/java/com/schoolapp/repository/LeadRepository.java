package com.schoolapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.schoolapp.entity.Lead;
//import com.Crmemp.entity.Lead;

public interface LeadRepository extends JpaRepository<Lead, Integer> {

	boolean existsByPanNo(String panNo);
	boolean existsByCustomerNameIgnoreCaseAndContactNo(String customerName, long contactNo);



}

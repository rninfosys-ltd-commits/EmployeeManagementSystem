package com.schoolapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.schoolapp.entity.OutWorkMaterial;


	@Repository
	public interface OutWorkMaterialRepo extends JpaRepository<OutWorkMaterial, Integer> {

	}
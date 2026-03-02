package com.schoolapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.schoolapp.entity.OutWorkMaterialMaster;

	@Repository
	public interface OutWorkMaterialMasterrRepo extends JpaRepository<OutWorkMaterialMaster, Integer> {

	}
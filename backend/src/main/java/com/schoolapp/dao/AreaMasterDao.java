package com.schoolapp.dao;

import java.sql.Date;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.schoolapp.entity.AreaMaster;
import com.schoolapp.repository.AreaMasterRepo;

@Component
public class AreaMasterDao {
	@Autowired
	AreaMasterRepo areaMasterRepo;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public AreaMaster saveAreaMaster(AreaMaster areaMaster) {
		System.out.println("Data inserted successfully...");
		return areaMasterRepo.save(areaMaster);
	}

	public List<AreaMaster> getAllAreaMaster() {
		return areaMasterRepo.findAll();
	}

	public AreaMaster updateDeleteAreaMaster(AreaMaster areaMaster) {

		Integer areaMasterId = areaMaster.getAreaMasterId();
		int updatedBy = areaMaster.getUpdatedBy();
		Date updatedDate = (Date) areaMaster.getUpdatedDate();
		int orgId = areaMaster.getOrgId();
		int isActive = areaMaster.getIsActive();

		String query = "update area_master set is_active=?, updated_by=?,updated_date=? where area_master_id=? and org_id=?";
		jdbcTemplate.update(query, isActive, updatedBy, updatedDate, areaMasterId, orgId);
		System.out.println("Record updated");
		return areaMasterRepo.save(areaMaster);
	}

	public AreaMaster findAreaMasterById(int AreaMasterId) {
		return (AreaMaster) areaMasterRepo.findById(AreaMasterId).get();
	}

	public String deleteAreaMasterById(int AreaMasterId) {
		areaMasterRepo.deleteById(AreaMasterId);
		return "deleted";
	}
}

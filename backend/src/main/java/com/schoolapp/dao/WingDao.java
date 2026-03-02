package com.schoolapp.dao;

import java.sql.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.schoolapp.entity.Wing;
import com.schoolapp.repository.WingRepo;

@Component
public class WingDao {
	@Autowired
	WingRepo wingRepo;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public Wing saveWing(Wing wing) {
		System.out.println("Data inserted successfully...");
		return wingRepo.save(wing);
	}

	public String updateWing(Wing wing) {
		Integer wingId = wing.getWingId();

		String wingName = wing.getWingName();
		int projectId = wing.getProjectId();
		int siteId = wing.getSiteId();
		int srNO = wing.getSrNO();
		int noOfFlower = wing.getNoOfFlower();
		int userId = wing.getUserId();
		int orgId = wing.getOrgId();
		int branchId = wing.getBranchId();
		Date createdDate = wing.getCreatedDate();
		int updatedBy = wing.getUserId();
		Date updatedDate = wing.getUpdatedDate();
		int isActive = wing.getIsActive();

		String sql = "update wing set wing_id = ? , branch_id = ? , created_date = ? , is_active = ? , no_of_flower = ? ,"
				+ "project_id = ? , srno = ? , updated_by = ? , updated_date = ? , user_id = ? , wing_name = ? , site_id = ?"
				+ " where wing_id= ? and org_id = ?";

		jdbcTemplate.update(sql, wingId, branchId, createdDate, isActive, noOfFlower, projectId, srNO, updatedBy,
				updatedDate, userId, wingName, siteId, wingId, orgId);

		return "Record updated..!";
	}

	public Wing updateDeleteWing(Wing wing) {
		Integer wingId = wing.getWingId();

		int updatedBy = wing.getUpdatedBy();
		Date updatedDate = wing.getUpdatedDate();
		int orgId = wing.getOrgId();
		int isActive = wing.getIsActive();

		String sql = "update wing set is_active=?, updated_by=?,updated_date=? " + "where wing_id=? and org_id=?";
		jdbcTemplate.update(sql, isActive, updatedBy, updatedDate, wingId, orgId);
		return wingRepo.save(wing);
	}

	public List<Wing> getAllWing() {
		return wingRepo.findAll();
	}

	public Wing findWingById(int wing) {
		return wingRepo.findById(wing).get();
	}

	public String deleteWingById(int wing) {
		wingRepo.deleteById(wing);
		return "deleted";
	}

}

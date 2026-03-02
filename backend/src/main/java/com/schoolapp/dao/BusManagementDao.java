package com.schoolapp.dao;

import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.springframework.jdbc.core.JdbcTemplate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.schoolapp.entity.BusManagement;

@Component
public class BusManagementDao {
	@Autowired
	com.schoolapp.repository.BusManagementRepo BusManagementRepo;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public BusManagement saveBusManagement(BusManagement BusManagement) {
		return BusManagementRepo.save(BusManagement);
	}

	public String getAllBusManagement(BusManagement BusManagement) {
		int orgId = BusManagement.getOrgId();
		String sql = "SELECT * FROM bus_management where org_id=?";
		List<Map<String, Object>> result = jdbcTemplate.queryForList(sql, orgId);
		return new JSONArray(result).toString();
	}

	public BusManagement findBusManagementById(int BusManagementId) {
		return BusManagementRepo.findById(BusManagementId).get();
	}

	public String deleteBusManagementById(int BusManagementId) {
		BusManagementRepo.deleteById(BusManagementId);
		return "deleted";
	}
}

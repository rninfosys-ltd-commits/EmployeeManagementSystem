package com.schoolapp.dao;

import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.schoolapp.entity.DispatchMaster;
import com.schoolapp.repository.DispatchMasterRepo;

@Component
public class DispatchMasterDao {

	@Autowired
	DispatchMasterRepo DispatchMasterRepo;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public DispatchMaster saveDispatchMaster(DispatchMaster DispatchMaster) {
		System.out.println("Data inserted successfully...");
		return DispatchMasterRepo.save(DispatchMaster);
	}

	public String getAllDispatchMaster(DispatchMaster DispatchMaster) {
		int orgId = DispatchMaster.getOrgId();
		String sql = "SELECT * FROM dispatch_master where org_id = ?";
		List<Map<String, Object>> result = jdbcTemplate.queryForList(sql, orgId);
		return new JSONArray(result).toString();
	}

	public DispatchMaster updateDeleteDispatchMaster(DispatchMaster DispatchMaster) {
		return DispatchMasterRepo.save(DispatchMaster);
	}

	public DispatchMaster findDispatchMasterById(int DispatchMasterId) {
		return (DispatchMaster) DispatchMasterRepo.findById(DispatchMasterId).get();
	}

	public String deleteDispatchMasterById(int DispatchMasterId) {
		DispatchMasterRepo.deleteById(DispatchMasterId);
		return "deleted";
	}

}

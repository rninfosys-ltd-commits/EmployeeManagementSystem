package com.schoolapp.dao;

import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.schoolapp.entity.ReasourceMaster;
import com.schoolapp.repository.ReasourceMasterRepo;

@Component
public class ReasourceMasterDao {
	@Autowired
	ReasourceMasterRepo ReasourceMasterRepo;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public ReasourceMaster saveReasourceMaster(ReasourceMaster ReasourceMaster) {
		return ReasourceMasterRepo.save(ReasourceMaster);
	}

	public String getAllReasourceMaster(ReasourceMaster ReasourceMaster) {
		int orgId = ReasourceMaster.getOrgId();
		String sql = "SELECT reasource_id, branch_id, created_date, is_active, org_id, reasource_name, updated_by, user_id FROM schooldb.reasource_master where org_id=?";
		List<Map<String, Object>> result = jdbcTemplate.queryForList(sql, orgId);
		return new JSONArray(result).toString();
	}

	public ReasourceMaster findReasourceMasterById(int ReasourceMasterId) {
		return ReasourceMasterRepo.findById(ReasourceMasterId).get();
	}

	public String deleteReasourceMasterById(int ReasourceMasterId) {
		ReasourceMasterRepo.deleteById(ReasourceMasterId);
		return "deleted";
	}
}

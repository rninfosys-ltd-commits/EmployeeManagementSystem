package com.schoolapp.dao;

import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.schoolapp.entity.ClassMaster;
import com.schoolapp.repository.ClassMasterRepo;

@Component
public class ClassMasterDao {

	@Autowired
	ClassMasterRepo classMasterRepo;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public ClassMaster saveClassMaster(ClassMaster classMaster) {
		return classMasterRepo.save(classMaster);
	}

	public String getAllClassMaster(ClassMaster classMaster) {
		int orgId = classMaster.getOrgId();
		String sql = "SELECT * FROM class_master where org_id=?";
		List<Map<String, Object>> result = jdbcTemplate.queryForList(sql, orgId);
		return new JSONArray(result).toString();
	}

	public ClassMaster findClassMasterById(int classMasterId) {
		return classMasterRepo.findById(classMasterId).get();
	}

	public String deleteClassMasterById(int classMasterId) {
		classMasterRepo.deleteById(classMasterId);
		return "deleted";
	}
}

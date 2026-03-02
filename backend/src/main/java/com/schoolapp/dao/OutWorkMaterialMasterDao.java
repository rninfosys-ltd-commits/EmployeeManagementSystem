package com.schoolapp.dao;

import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.schoolapp.entity.OutWorkMaterialMaster;
import com.schoolapp.repository.OutWorkMaterialMasterrRepo;

@Component
public class OutWorkMaterialMasterDao {

	@Autowired
	OutWorkMaterialMasterrRepo OutWorkMaterialMasterRepo;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public OutWorkMaterialMaster saveOutWorkMaterialMaster(OutWorkMaterialMaster OutWorkMaterialMaster) {
		System.out.println("Data inserted successfully...");
		return OutWorkMaterialMasterRepo.save(OutWorkMaterialMaster);
	}

	public String getAllOutWorkMaterialMaster(OutWorkMaterialMaster OutWorkMaterialMaster) {
		int orgId = OutWorkMaterialMaster.getOrgId();
		String sql = "SELECT * FROM out_work_material_master where org_id = ?";
		List<Map<String, Object>> result = jdbcTemplate.queryForList(sql, orgId);
		return new JSONArray(result).toString();
	}

	public OutWorkMaterialMaster updateDeleteOutWorkMaterialMaster(OutWorkMaterialMaster OutWorkMaterialMaster) {
		return OutWorkMaterialMasterRepo.save(OutWorkMaterialMaster);
	}

	public OutWorkMaterialMaster findOutWorkMaterialMasterById(int OutWorkMaterialMasterId) {
		return (OutWorkMaterialMaster) OutWorkMaterialMasterRepo.findById(OutWorkMaterialMasterId).get();
	}

	public String deleteOutWorkMaterialMasterById(int OutWorkMaterialMasterId) {
		OutWorkMaterialMasterRepo.deleteById(OutWorkMaterialMasterId);
		return "deleted";
	}

}

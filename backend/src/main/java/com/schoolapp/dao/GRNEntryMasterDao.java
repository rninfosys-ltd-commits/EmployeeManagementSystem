package com.schoolapp.dao;

import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.schoolapp.entity.GRNEntryMaster;
import com.schoolapp.repository.GRNEntryMasterRepo;

@Component
public class GRNEntryMasterDao {

	@Autowired
	GRNEntryMasterRepo GRNEntryMasterRepo;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public GRNEntryMaster saveGRNEntryMaster(GRNEntryMaster GRNEntryMaster) {
		System.out.println("Data inserted successfully...");
		return GRNEntryMasterRepo.save(GRNEntryMaster);
	}

	public String getAllGRNEntryMaster(GRNEntryMaster GRNEntryMaster) {
		int orgId = GRNEntryMaster.getOrgId();
		String sql = "SELECT c.cust_name, m.grnentry_master_id, m.created_date, m.purchase_order_id "
				+ "FROM grnentry_master m left join grnentry g on m.grnentry_master_id = g.grnentry_id "
				+ "Inner join customer c on g.customer_id = c.cust_id where m.org_id = ?";
		List<Map<String, Object>> result = jdbcTemplate.queryForList(sql, orgId);
		return new JSONArray(result).toString();
	}

	public GRNEntryMaster updateDeleteGRNEntryMaster(GRNEntryMaster GRNEntryMaster) {
		return GRNEntryMasterRepo.save(GRNEntryMaster);
	}

	public GRNEntryMaster findGRNEntryMasterById(int GRNEntryMasterId) {
		return (GRNEntryMaster) GRNEntryMasterRepo.findById(GRNEntryMasterId).get();
	}

	public String deleteGRNEntryMasterById(int GRNEntryMasterId) {
		GRNEntryMasterRepo.deleteById(GRNEntryMasterId);
		return "deleted";
	}

}

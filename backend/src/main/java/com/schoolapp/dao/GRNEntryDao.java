package com.schoolapp.dao;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import com.schoolapp.entity.GRNEntry;
import com.schoolapp.repository.GRNEntryRepo;

@Component
public class GRNEntryDao {
	@Autowired
	GRNEntryRepo GRNEntryRepo;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	int existInqMst = 0;

	public List<GRNEntry> saveGRNEntry(List<GRNEntry> GRNEntry) {
		for (GRNEntry al2 : GRNEntry) {
			int srValide = al2.getGRNEntryMasterId();
			if (srValide == 0) {
				int branchId = al2.getBranchId();
				java.sql.Date createdDate = (java.sql.Date) al2.getCreatedDate();
				int orgId = al2.getOrgId();
				int purchaseOrder_id = al2.getGrnentryId();
				int updatedBy = al2.getUpdatedBy();
				java.sql.Date updatedDate = (java.sql.Date) al2.getUpdatedDate();
				int userId = al2.getUserId();

				String sql = "insert into grnentry_master( branch_id, created_date,org_id, purchase_order_id, updated_by, updated_date, user_id) values(?,?,?,?,?,?,?)";

				KeyHolder keyHolder = new GeneratedKeyHolder();
				jdbcTemplate.update(connection -> {
					PreparedStatement pst = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
					pst.setInt(1, branchId);
					pst.setDate(2, createdDate);
					pst.setInt(3, orgId);
					pst.setInt(4, purchaseOrder_id);
					pst.setInt(5, updatedBy);
					pst.setDate(6, updatedDate);
					pst.setInt(7, userId);
					return pst;
				}, keyHolder);

				int GRNEntryId = keyHolder.getKey().intValue();
				for (GRNEntry al3 : GRNEntry) {
					al3.setGRNEntryMasterId(GRNEntryId);
					existInqMst = GRNEntryId;
				}
			}
			System.out.println("Data inserted successfully...");
			return GRNEntryRepo.saveAll(GRNEntry);
		}
		return GRNEntry;
	}

	public String getAllGRNEntry(GRNEntry GRNEntry) {
		int orgId = GRNEntry.getOrgId();
		String sql = "SELECT * FROM grnentry where org_id = ?";
		List<Map<String, Object>> result = jdbcTemplate.queryForList(sql, orgId);
		return new JSONArray(result).toString();
	}

	public GRNEntry updateDeleteGRNEntry(GRNEntry GRNEntry) {
		return GRNEntryRepo.save(GRNEntry);
	}

	public GRNEntry findGRNEntryById(int GRNEntryId) {
		return (GRNEntry) GRNEntryRepo.findById(GRNEntryId).get();
	}

	public String deleteGRNEntryById(int GRNEntryId) {
		GRNEntryRepo.deleteById(GRNEntryId);
		return "deleted";
	}
}

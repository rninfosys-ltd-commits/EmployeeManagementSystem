package com.schoolapp.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.schoolapp.entity.FeesStructure;
import com.schoolapp.repository.FeesStructureRepo;

@Component
public class FeesStructureDao {
	@Autowired
	FeesStructureRepo FeesStructureRepo;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public List<FeesStructure> saveFeesStructure(List<FeesStructure> al) {
		List<FeesStructure> newAl = new ArrayList<>();
		String sql = "SELECT count(1) FROM fees_structure WHERE class_id = ? AND finance_year = ? AND reasource_id = ?";

		for (FeesStructure fee : al) {
			Integer count = jdbcTemplate.queryForObject(sql, Integer.class, fee.getClassId(), fee.getFinanceYear(),
					fee.getReasourceId());

			if (count == null || count == 0) {
				newAl.add(fee);
			} else {
				System.out.println("Duplicate found for classId: " + fee.getClassId() + ", financeYear: "
						+ fee.getFinanceYear() + ", reasourceId: " + fee.getReasourceId());
			}
		}

		if (!newAl.isEmpty()) {
			return (List<FeesStructure>) FeesStructureRepo.saveAll(newAl);
		}
		return new ArrayList<>();
	}

	public String getAllFeesStructure(FeesStructure FeesStructure) {
		int orgId = FeesStructure.getOrgId();
		String sql = "SELECT cm.class_name, rm.reasource_name, f.fees_structure_id, f.branch_id, f.charges, f.class_id, f.created_date, f.finance_year, f.is_active, f.org_id, f.reasource_id, f.tr_date, f.updated_by, f.updated_date, f.user_id "
				+ "FROM fees_structure f " + "inner join class_master cm on f.class_id=cm.class_id "
				+ "inner join reasource_master rm on f.reasource_id=rm.reasource_id " + "where f.org_id = ?";
		List<Map<String, Object>> result = jdbcTemplate.queryForList(sql, orgId);
		return new JSONArray(result).toString();
	}

	public FeesStructure findFeesStructureById(int FeesStructureId) {
		return FeesStructureRepo.findById(FeesStructureId).get();
	}

	public String deleteFeesStructureById(int FeesStructureId) {
		FeesStructureRepo.deleteById(FeesStructureId);
		return "deleted";
	}
}

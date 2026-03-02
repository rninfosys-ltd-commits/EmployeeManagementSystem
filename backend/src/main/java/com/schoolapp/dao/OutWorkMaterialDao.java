package com.schoolapp.dao;

import java.sql.Statement;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import com.schoolapp.entity.OutWorkMaterial;
import com.schoolapp.repository.OutWorkMaterialRepo;

@Component
public class OutWorkMaterialDao {

	@Autowired
	OutWorkMaterialRepo OutWorkMaterialRepo;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	int existInqMst = 0;

	public List<OutWorkMaterial> saveOutWork(List<OutWorkMaterial> OutWork) {

		for (OutWorkMaterial al2 : OutWork) {
			int srValide = al2.getOutWorkMaterialId();

			if (srValide == 0) {
				String sql = "insert into out_work_material_master(branch_id, created_date, org_id, purchase_order_id, updated_by, updated_date, user_id) values(?,?,?,?,?,?,?)";
				KeyHolder keyHolder = new GeneratedKeyHolder();

				jdbcTemplate.update(connection -> {
					java.sql.PreparedStatement pst = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
					pst.setInt(1, al2.getBranchId());
					pst.setDate(2, al2.getCreatedDate());
					pst.setInt(3, al2.getOrgId());
					pst.setInt(4, al2.getPurchaseOrderId());
					pst.setInt(5, al2.getUpdatedBy());
					pst.setDate(6, al2.getUpdatedDate());
					pst.setInt(7, al2.getUserId());
					return pst;
				}, keyHolder);

				int OutWorkId = keyHolder.getKey().intValue();
				for (OutWorkMaterial al3 : OutWork) {
					al3.setOutWorkMaterialId(OutWorkId);
					existInqMst = OutWorkId;
				}
			}
		}

		System.out.println("Data inserted successfully...");
		return OutWorkMaterialRepo.saveAll(OutWork);
	}

	public String getAllOutWorkMaterial(OutWorkMaterial OutWorkMaterial) {
		int orgId = OutWorkMaterial.getOrgId();
		String sql = "SELECT * FROM out_work_material where org_id = ?";
		List<Map<String, Object>> result = jdbcTemplate.queryForList(sql, orgId);
		return new JSONArray(result).toString();
	}

	public OutWorkMaterial updateDeleteOutWorkMaterial(OutWorkMaterial OutWorkMaterial) {
		return OutWorkMaterialRepo.save(OutWorkMaterial);
	}

	public OutWorkMaterial findOutWorkMaterialById(int OutWorkMaterialId) {
		return (OutWorkMaterial) OutWorkMaterialRepo.findById(OutWorkMaterialId).get();
	}

	public String deleteOutWorkMaterialById(int OutWorkMaterialId) {
		OutWorkMaterialRepo.deleteById(OutWorkMaterialId);
		return "deleted";
	}

}

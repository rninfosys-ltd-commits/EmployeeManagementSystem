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

import com.schoolapp.entity.Dispatch;
import com.schoolapp.repository.DispatchRepo;

@Component
public class DispatchDao {

	@Autowired
	DispatchRepo DispatchRepo;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	int existInqMst = 0;

	public List<Dispatch> saveDispatch(List<Dispatch> Dispatch) {

		for (Dispatch al2 : Dispatch) {
			int srValide = al2.getDispatchMasterId();

			if (srValide == 0) {
				String sql = "insert into dispatch_master(branch_id, created_date, org_id, purchase_order_id, updated_by, updated_date, user_id) values(?,?,?,?,?,?,?)";
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

				int DispatchId = keyHolder.getKey().intValue();
				for (Dispatch al3 : Dispatch) {
					al3.setDispatchMasterId(DispatchId);
					existInqMst = DispatchId;
				}
			}
		}

		System.out.println("Data inserted successfully...");
		return DispatchRepo.saveAll(Dispatch);
	}

	public String getAllDispatch(Dispatch Dispatch) {
		int orgId = Dispatch.getOrgId();
		String sql = "SELECT * FROM dispatch where org_id = ?";
		List<Map<String, Object>> result = jdbcTemplate.queryForList(sql, orgId);
		return new JSONArray(result).toString();
	}

	public Dispatch updateDeleteDispatch(Dispatch Dispatch) {
		return DispatchRepo.save(Dispatch);
	}

	public Dispatch findDispatchById(int DispatchId) {
		return (Dispatch) DispatchRepo.findById(DispatchId).get();
	}

	public String deleteDispatchById(int DispatchId) {
		DispatchRepo.deleteById(DispatchId);
		return "deleted";
	}
}

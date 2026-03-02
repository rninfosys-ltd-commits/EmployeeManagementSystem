package com.schoolapp.dao;

import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.schoolapp.entity.TcMaster;
import com.schoolapp.repository.TcMasterRepo;

@Component
public class TcMasterDao {
	@Autowired
	TcMasterRepo TcMasterRepo;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public TcMaster saveTcMaster(TcMaster TcMaster) {
		TcMasterRepo.save(TcMaster);
		int orgId = TcMaster.getOrgId();
		int tcMasterId = TcMaster.getTcMasterId();

		String sql = "UPDATE tc_master AS t1"
				+ " JOIN (SELECT IFNULL(COUNT(tc_master_id), 0) + 1 AS total_count FROM tc_master"
				+ " WHERE org_id = ? AND tc_master_id < 1 ) AS t2"
				+ " SET t1.lcissue_no = t2.total_count WHERE t1.tc_master_id = ?";

		jdbcTemplate.update(sql, orgId, tcMasterId);
		return null;
	}

	public String getAllTcMaster(TcMaster TcMaster) {
		int orgId = TcMaster.getOrgId();
		String sql = "SELECT concat(ad.first_name, ' ', ad.middle_name, ' ', ad.last_name) as name, tm.tc_master_id, tm.branch_id, tm.conduct, tm.created_date, tm.date_of_leaving, tm.is_active, tm.org_id, tm.reason_of_leaving, tm.remark, tm.tc_std_name, tm.updated_by, tm.updated_date, tm.user_id, tm.taluka_name,"
				+ " tm.dist_name, tm.lcissue_no FROM tc_master tm"
				+ " LEFT JOIN admission ad ON tm.tc_std_name=ad.admission_id"
				+ " WHERE tm.org_id = ?";

		List<Map<String, Object>> result = jdbcTemplate.queryForList(sql, orgId);
		return new JSONArray(result).toString();
	}

	public TcMaster updateTcMaster(TcMaster TcMaster) {

		return TcMasterRepo.save(TcMaster);
	}

	public TcMaster findTcMasterById(int TcMasterId) {
		return TcMasterRepo.findById(TcMasterId).get();
	}

	public String deleteTcMasterById(int TcMasterId) {
		TcMasterRepo.deleteById(TcMasterId);
		return "deleted";
	}
}

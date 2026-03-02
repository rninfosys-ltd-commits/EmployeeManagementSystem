package com.schoolapp.dao;

import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.schoolapp.entity.StdAnnualFees;
import com.schoolapp.repository.StdAnnualFeesRepo;

@Component
public class StdAnnualFeesDao {
	@Autowired
	StdAnnualFeesRepo StdAnnualFeesRepo;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public StdAnnualFees saveStdAnnualFees(StdAnnualFees StdAnnualFees) {
		return StdAnnualFeesRepo.save(StdAnnualFees);
	}

	public String getAllStdAnnualFees(StdAnnualFees StdAnnualFees) {
		int orgId = StdAnnualFees.getOrgId();
		String sql = "SELECT concat(ad.first_name, ' ', ad.middle_name, ' ', ad.last_name) as student_name, cm.class_name, sf.std_annual_fees_id, sf.branch_id, sf.charges, sf.class_id, sf.created_date, sf.fees_structure_id, sf.finance_year,"
				+ " sf.is_active, sf.org_id, sf.reasource_id, sf.s_id, sf.tr_date, sf.updated_by, sf.updated_date, sf.user_id"
				+ " FROM std_annual_fees sf"
				+ " INNER JOIN admission ad ON sf.s_id = ad.admission_id"
				+ " INNER JOIN class_master cm ON ad.class_id = cm.class_id"
				+ " WHERE sf.org_id = ?";

		List<Map<String, Object>> result = jdbcTemplate.queryForList(sql, orgId);
		return new JSONArray(result).toString();
	}

	public String getYearWiseStdOutstanding(StdAnnualFees StdAnnualFees) {
		int financialYear = StdAnnualFees.getFinanceYear();
		int sId = StdAnnualFees.getSid();
		int orgID = StdAnnualFees.getOrgId();

		String sql = "SELECT a.charges - b.rcv_charges AS charges FROM ("
				+ " SELECT ad.admission_id, ifnull(sum(sf.charges), 0) AS charges"
				+ " FROM std_annual_fees sf"
				+ " LEFT JOIN admission ad ON sf.s_id = ad.admission_id"
				+ " WHERE sf.finance_year = ? AND ad.org_id = ? AND ad.admission_id = ?"
				+ " GROUP BY ad.admission_id) AS a,"
				+ " (SELECT ifnull(sum(std.recived_amt), 0) AS rcv_charges"
				+ " FROM std_annual_fees sf"
				+ " LEFT JOIN std_transaction_detailes std ON sf.std_annual_fees_id = std.std_annual_fees_id"
				+ " WHERE sf.finance_year = ? AND sf.org_id = ? AND sf.s_id = ?) AS b";

		List<Map<String, Object>> result = jdbcTemplate.queryForList(sql, financialYear, orgID, sId, financialYear,
				orgID, sId);
		return new JSONArray(result).toString();
	}

	public StdAnnualFees findStdAnnualFeesById(int StdAnnualFeesId) {
		return StdAnnualFeesRepo.findById(StdAnnualFeesId).get();
	}

	public String deleteStdAnnualFeesById(int StdAnnualFeesId) {
		StdAnnualFeesRepo.deleteById(StdAnnualFeesId);
		return "deleted";
	}
}

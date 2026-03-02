package com.schoolapp.dao;

import java.sql.Date;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.schoolapp.entity.Dashboard;
import com.schoolapp.entity.StdAnnualFees;
import com.schoolapp.repository.DashboardRepo;

@Component
public class DashboardDao {
	@Autowired
	DashboardRepo dashboardRepo;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public Dashboard saveDashboard(Dashboard dashboard) {
		return dashboardRepo.save(dashboard);
	}

	public List<Dashboard> getAllDashboard() {

		return dashboardRepo.findAll();
	}

	public Dashboard findDashboardById(int dashId) {
		return dashboardRepo.findById(dashId).get();
	}

	public String deleteDashboardById(int dashId) {
		dashboardRepo.deleteById(dashId);
		return "deleted";
	}

	// ---------- All Important apis ---------------------------

	public String AllStudentYearWise(StdAnnualFees stdAnnualFees) {
		int financeYear = stdAnnualFees.getFinanceYear();
		int orgId = stdAnnualFees.getOrgId();
		String sql = "SELECT finance_year, count(distinct s_id) as total FROM std_annual_fees WHERE org_id = ? AND finance_year = ? GROUP BY finance_year";
		List<Map<String, Object>> result = jdbcTemplate.queryForList(sql, orgId, financeYear);
		return new JSONArray(result).toString();
	}

	public String AllStudentClassWise(StdAnnualFees stdAnnualFees) {
		int financeYear = stdAnnualFees.getFinanceYear();
		int orgId = stdAnnualFees.getOrgId();
		String sql = "SELECT sf.class_id, cm.class_name, ifnull(count(distinct sf.s_id), 0) as total_student, round(ifnull(sum(sf.charges), 0)) Total_Annual_Fees,"
				+ " ifnull(sum(st.recived_amt), 0) As Total_Recieved_Fees,"
				+ " ifnull(sum(sf.charges), 0) - ifnull(sum(st.recived_amt), 0) as Recieved"
				+ " FROM std_annual_fees sf"
				+ " LEFT JOIN std_transaction_detailes st on sf.std_annual_fees_id = st.std_annual_fees_id"
				+ " LEFT JOIN class_master cm on sf.class_id=cm.class_id"
				+ " WHERE sf.org_id = ? AND sf.finance_year = ?"
				+ " GROUP BY sf.class_id, cm.class_name";

		List<Map<String, Object>> result = jdbcTemplate.queryForList(sql, orgId, financeYear);
		return new JSONArray(result).toString();
	}

	public String TotalFeesClassWise(StdAnnualFees stdAnnualFees) {
		int financeYear = stdAnnualFees.getFinanceYear();
		int orgId = stdAnnualFees.getOrgId();
		String sql = "SELECT finance_year, class_id, sum(charges) as sum FROM std_annual_fees"
				+ " WHERE org_id = ? AND finance_year = ? GROUP BY finance_year, class_id";
		List<Map<String, Object>> result = jdbcTemplate.queryForList(sql, orgId, financeYear);
		return new JSONArray(result).toString();
	}

	public String StdResourceWisePendingAmt(StdAnnualFees stdAnnualFees) {
		int financeYear = stdAnnualFees.getFinanceYear();
		int orgId = stdAnnualFees.getOrgId();
		String sql = "SELECT sf.s_id, ad.first_name, sf.std_annual_fees_id, sum(sf.charges) Total_Annual_Fees,"
				+ " sum(st.recived_amt) As Total_Recieved_Fees,"
				+ " sum(sf.charges) - sum(st.recived_amt) as pending FROM std_annual_fees sf"
				+ " INNER JOIN std_transaction_detailes st on sf.std_annual_fees_id = st.std_annual_fees_id"
				+ " INNER JOIN admission ad on sf.s_id=ad.admission_id WHERE sf.org_id = ? AND sf.finance_year = ?"
				+ " GROUP BY sf.s_id, ad.first_name, sf.std_annual_fees_id, sf.charges, st.recived_amt";

		List<Map<String, Object>> result = jdbcTemplate.queryForList(sql, orgId, financeYear);
		return new JSONArray(result).toString();
	}

	public String AnnualFeesStatus(StdAnnualFees stdAnnualFees) {
		int financeYear = stdAnnualFees.getFinanceYear();
		int orgId = stdAnnualFees.getOrgId();
		String sql = "SELECT sum(sf.charges) Total_Annual_Fees, sum(st.recived_amt) As Total_Recieved_Fees,"
				+ " sum(sf.charges) - sum(st.recived_amt) as Pending FROM std_annual_fees sf"
				+ " INNER JOIN std_transaction_detailes st on sf.std_annual_fees_id = st.std_annual_fees_id"
				+ " WHERE sf.org_id = ? AND sf.finance_year = ?";

		List<Map<String, Object>> result = jdbcTemplate.queryForList(sql, orgId, financeYear);
		return new JSONArray(result).toString();
	}

	public String AnnualResourceTrStatus(StdAnnualFees stdAnnualFees) {
		int financeYear = stdAnnualFees.getFinanceYear();
		int orgId = stdAnnualFees.getOrgId();
		String sql = "SELECT rm.reasource_id, rm.reasource_name, sum(sf.charges) Total_Annual_Fees,"
				+ " sum(st.recived_amt) As Total_Recieved_Fees,"
				+ " sum(sf.charges) - sum(st.recived_amt) as pending FROM std_annual_fees sf"
				+ " INNER JOIN std_transaction_detailes st on sf.std_annual_fees_id = st.std_annual_fees_id"
				+ " INNER JOIN reasource_master rm on sf.reasource_id=rm.reasource_id"
				+ " WHERE sf.org_id = ? AND sf.finance_year = ?"
				+ " GROUP BY rm.reasource_id, rm.reasource_name";

		List<Map<String, Object>> result = jdbcTemplate.queryForList(sql, orgId, financeYear);
		return new JSONArray(result).toString();
	}

	public String AnnualClassReasourceWiseStatus(StdAnnualFees stdAnnualFees) {
		int financeYear = stdAnnualFees.getFinanceYear();
		int orgId = stdAnnualFees.getOrgId();
		String sql = "SELECT fs.class_id, cm.class_name, sf.reasource_id, rm.reasource_name,"
				+ " ifnull(sum(sf.charges), 0) Total_Annual_Fees,"
				+ " ifnull(sum(st.recived_amt), 0) As Total_Recieved_Fees,"
				+ " ifnull(sum(sf.charges), 0) - ifnull(sum(st.recived_amt), 0) as pending"
				+ " FROM fees_structure fs"
				+ " INNER JOIN class_master cm ON fs.class_id=cm.class_id"
				+ " INNER JOIN reasource_master rm ON fs.reasource_id=rm.reasource_id"
				+ " INNER JOIN std_annual_fees sf ON cm.class_id=sf.class_id AND rm.reasource_id=sf.reasource_id"
				+ " LEFT JOIN std_transaction_detailes st ON sf.std_annual_fees_id = st.std_annual_fees_id"
				+ " WHERE sf.org_id = ? AND sf.finance_year = ?"
				+ " GROUP BY fs.class_id, cm.class_name, sf.reasource_id, rm.reasource_name";

		List<Map<String, Object>> result = jdbcTemplate.queryForList(sql, orgId, financeYear);
		return new JSONArray(result).toString();
	}

	public String datewiseFeesCollRpt(StdAnnualFees stdAnnualFees) {
		Date fromDate = stdAnnualFees.getCreatedDate();
		Date toDate = stdAnnualFees.getUpdatedDate();
		int orgId = stdAnnualFees.getOrgId();

		String sql = "SELECT ifnull(concat(ad.first_name, ' ', ad.middle_name, ' ', ad.last_name), '') as name, usr.user_name, tm.transaction_master_id,"
				+ " DATE_FORMAT(tm.created_date, '%d/%m/%Y') AS created_date, tm.description, tm.finance_year, tm.org_id, tm.recived_amt, tm.s_id, tm.tr_date, tm.updated_by,"
				+ " tm.updated_date, tm.user_id FROM transaction_master tm"
				+ " LEFT JOIN user usr ON tm.org_id=usr.org_id AND tm.user_id=usr.u_id"
				+ " LEFT JOIN admission ad ON tm.org_id=ad.org_id AND tm.s_id=ad.admission_id"
				+ " WHERE tm.org_id = ? AND tm.created_date BETWEEN ? AND ?"
				+ " GROUP BY ad.middle_name, ad.last_name, ad.first_name, usr.user_name, tm.transaction_master_id, tm.created_date, tm.description, tm.finance_year, tm.org_id, tm.recived_amt, tm.s_id, tm.tr_date, tm.updated_by, tm.updated_date, tm.user_id"
				+ " ORDER BY tm.transaction_master_id";

		List<Map<String, Object>> result = jdbcTemplate.queryForList(sql, orgId, fromDate, toDate);
		return new JSONArray(result).toString();
	}

}

package com.schoolapp.dao;

import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.schoolapp.entity.LeadAccounts;
import com.schoolapp.repository.LeadAccountsRepo;

@Component
public class LeadAccountsDao {

	@Autowired
	LeadAccountsRepo leadAccountsRepo;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public LeadAccounts saveLeadAccounts(LeadAccounts leadAccounts) {
		System.out.println("Data inserted successfully...");
		return leadAccountsRepo.save(leadAccounts);
	}

	public String getAllLeadAccounts() {
		String sql = "SELECT l.lead_id, l.area, l.assign_id, l.branch_id, l.budget, l.budget1, l.budget2, l.c_name, l.city_id, l.created_date, l.date, l.dist_id, l.email, l.fax,"
				+ " l.gst_no, l.income, l.income_source, l.invoice_address, l.is_active, l.notes, l.org_id, l.other_income, l.other_income_source, l.owner_contact,"
				+ " l.owner_name, l.pan_no, l.phone, l.resource_id, l.state_id, l.status_id, l.updated_by, l.updated_date, l.user_id, l.website,"
				+ " b.branch_name AS branch_name, u.user_name AS user_name, s.state_name AS state_name,"
				+ " d.dist_name AS dist_name, c.city_name AS city_name, r.resource_name AS source,"
				+ " ro.resource_name AS other_incomes, ri.resource_name AS rs_income "
				+ "FROM lead_accounts l "
				+ "INNER JOIN organization o ON l.org_id = o.org_id "
				+ "INNER JOIN branch b ON l.branch_id = b.branch_id "
				+ "INNER JOIN user u ON l.user_id = u.u_id "
				+ "INNER JOIN state s ON l.state_id = s.state_id "
				+ "INNER JOIN district d ON l.dist_id = d.dist_id "
				+ "INNER JOIN city c ON l.city_id = c.city_id "
				+ "INNER JOIN resource r ON l.resource_id = r.resource_id "
				+ "INNER JOIN resource ro ON l.other_income_source = ro.resource_id "
				+ "INNER JOIN resource ri ON l.income_source = ri.resource_id";

		List<Map<String, Object>> result = jdbcTemplate.queryForList(sql);
		return new JSONArray(result).toString();
	}

	public LeadAccounts updateDeleteLeadAccounts(LeadAccounts leadAccounts) {
		Integer leadId = leadAccounts.getLeadId();
		int updatedBy = leadAccounts.getUpdatedBy();
		java.util.Date updatedDate = leadAccounts.getUpdatedDate();
		int orgId = leadAccounts.getOrgId();
		int isActive = leadAccounts.getIsActive();

		String sql = "UPDATE lead_accounts SET is_active = ?, updated_by = ?, updated_date = ? "
				+ "WHERE lead_id = ? AND org_id = ?";

		jdbcTemplate.update(sql, isActive, updatedBy, updatedDate, leadId, orgId);

		return leadAccountsRepo.save(leadAccounts);
	}

	public LeadAccounts findLeadAccountsById(int leadAccountId) {
		return leadAccountsRepo.findById(leadAccountId).get();
	}

	public String deleteLeadAccountById(int leadAccountId) {
		leadAccountsRepo.deleteById(leadAccountId);
		return "deleted";
	}
}

package com.schoolapp.dao;

import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.schoolapp.entity.QuotationMaster;
import com.schoolapp.repository.QuotationMasterRepo;

@Component
public class QuotationMasterDao {

	@Autowired
	QuotationMasterRepo quotationMasterRepo;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public QuotationMaster saveQuotationMaster(QuotationMaster quotationMaster) {
		System.out.println("Data inserted successfully...");
		System.out.println(quotationMaster);
		return quotationMasterRepo.save(quotationMaster);
	}

	public QuotationMaster updateDeleteQuotationMaster(QuotationMaster quotationMaster) {
		Integer quotationMasterId = quotationMaster.getQuotationMstId();
		int updatedBy = quotationMaster.getUpdatedBy();
		java.util.Date updatedDate = quotationMaster.getUpdatedDate();
		int orgId = quotationMaster.getOrgId();
		int isActive = quotationMaster.getIsActive();

		String query = "update quotation_master set is_active=?, updated_by=?,updated_date=? "
				+ "where quotation_id=? and org_id=?";
		jdbcTemplate.update(query, isActive, updatedBy, updatedDate, quotationMasterId, orgId);
		System.out.println("Record updated");
		return quotationMasterRepo.save(quotationMaster);
	}

	public String getAllQuotation(QuotationMaster quotationMaster) {
		int orgId = quotationMaster.getOrgId();
		int custId = quotationMaster.getCustomerId();
		String sql = "SELECT cu.cust_name, qm.quotation_mst_id, qm.date, qm.branch_id, qm.created_date, qm.customer_id, qm.is_active, "
				+ "qm.org_id, qm.srno, qm.total, qm.updated_by, qm.updated_date, qm.user_id FROM quotation_master qm "
				+ "inner join customer cu on qm.customer_id=cu.cust_id "
				+ "WHERE case when ? > 0 then qm.customer_id = ? else qm.customer_id > 0 end " + "AND qm.org_id = ?";
		List<Map<String, Object>> result = jdbcTemplate.queryForList(sql, custId, custId, orgId);
		return new JSONArray(result).toString();
	}

	public QuotationMaster findQuotationById(int InqId) {
		return quotationMasterRepo.findById(InqId).get();
	}

	public String deleteQuotationByID(int InqId) {
		quotationMasterRepo.deleteById(InqId);
		return "deleted";
	}

}

package com.schoolapp.dao;

import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.schoolapp.entity.PurchaseOrderMaster;

import com.schoolapp.repository.PurchaseOrderMasterRepo;

@Component
public class PurchaseOrderMasterDao {

	@Autowired
	PurchaseOrderMasterRepo PurchaseOrderMasterRepo;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public PurchaseOrderMaster savePurchaseOrderMaster(PurchaseOrderMaster PurchaseOrderMaster) {
		System.out.println("Data inserted successfully...");
		System.out.println(PurchaseOrderMaster);
		return PurchaseOrderMasterRepo.save(PurchaseOrderMaster);
	}

	public PurchaseOrderMaster updateDeletePurchaseOrderMaster(PurchaseOrderMaster PurchaseOrderMaster) {
		Integer PurchaseOrderMasterId = PurchaseOrderMaster.getPurchaseOrderMstId();
		int updatedBy = PurchaseOrderMaster.getUpdatedBy();
		java.util.Date updatedDate = PurchaseOrderMaster.getUpdatedDate();
		int orgId = PurchaseOrderMaster.getOrgId();
		int isActive = PurchaseOrderMaster.getIsActive();

		String query = "update Purchase_master set is_active=?, updated_by=?,updated_date=? where Purchase_id=? and org_id=?";
		jdbcTemplate.update(query, isActive, updatedBy, updatedDate, PurchaseOrderMasterId, orgId);
		System.out.println("Record updated");
		return PurchaseOrderMasterRepo.save(PurchaseOrderMaster);
	}

	public String getAllPurchase(PurchaseOrderMaster PurchaseOrderMaster) {
		int orgId = PurchaseOrderMaster.getOrgId();
		int custId = PurchaseOrderMaster.getCustomerId();
		String sql = "SELECT cu.cust_name, pom.purchase_id, pom.date, pom.branch_id, pom.created_date, pom.customer_id, pom.is_active, pom.org_id, pom.srno, pom.total, pom.updated_by, pom.updated_date, pom.user_id "
				+ "FROM Purchase_order_master pom INNER JOIN customer cu ON pom.customer_id = cu.cust_id "
				+ "WHERE case when ? > 0 then pom.customer_id = ? else pom.customer_id > 0 end AND pom.org_id = ?";
		List<Map<String, Object>> result = jdbcTemplate.queryForList(sql, custId, custId, orgId);
		return new JSONArray(result).toString();
	}

	public PurchaseOrderMaster findPurchaseById(int InqId) {
		return PurchaseOrderMasterRepo.findById(InqId).get();
	}

	public String deletePurchaseByID(int InqId) {
		PurchaseOrderMasterRepo.deleteById(InqId);
		return "deleted";
	}

}

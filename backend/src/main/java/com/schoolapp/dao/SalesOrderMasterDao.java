package com.schoolapp.dao;

import java.sql.Date;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.schoolapp.entity.SalesOrderMaster;
import com.schoolapp.repository.SalesOrderMasterRepo;

@Component
public class SalesOrderMasterDao {

	@Autowired
	SalesOrderMasterRepo salesOrderMasterRepo;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public SalesOrderMaster saveSalesOrderMaster(SalesOrderMaster salesOrderMaster) {
		System.out.println("Data inserted successfully...");
		System.out.println(salesOrderMaster);
		return salesOrderMasterRepo.save(salesOrderMaster);
	}

	public SalesOrderMaster updateDeleteSalesOrderMaster(SalesOrderMaster salesOrderMaster) {
		Integer salesOrderMasterId = salesOrderMaster.getSalesOrderMstId();
		int updatedBy = salesOrderMaster.getUpdatedBy();
		Date updatedDate = salesOrderMaster.getUpdatedDate();
		int orgId = salesOrderMaster.getOrgId();
		int isActive = salesOrderMaster.getIsActive();

		String query = "update sales_master set is_active=?, updated_by=?,updated_date=? "
				+ "where sales_id=? and org_id=?";
		jdbcTemplate.update(query, isActive, updatedBy, updatedDate, salesOrderMasterId, orgId);
		System.out.println("Record updated");
		return salesOrderMasterRepo.save(salesOrderMaster);
	}

	public String getAllSales(SalesOrderMaster salesOrderMaster) {
		int orgId = salesOrderMaster.getOrgId();
		int custId = salesOrderMaster.getCustomerId();
		String sql = "SELECT cu.cust_name, som.sales_order_mst_id, som.date, som.branch_id, som.created_date, som.customer_id, som.is_active, som.org_id, som.srno, som.total, som.updated_by, "
				+ "som.updated_date, som.user_id FROM  sales_order_master som "
				+ "inner join customer cu on som.customer_id=cu.cust_id "
				+ "WHERE case when ? > 0 then som.customer_id = ? else som.customer_id > 0 end "
				+ "AND som.org_id = ?";
		List<Map<String, Object>> result = jdbcTemplate.queryForList(sql, custId, custId, orgId);
		return new JSONArray(result).toString();
	}

	public SalesOrderMaster findSalesById(int InqId) {
		return salesOrderMasterRepo.findById(InqId).get();
	}

	public String deleteSalesByID(int InqId) {
		salesOrderMasterRepo.deleteById(InqId);
		return "deleted";
	}

}

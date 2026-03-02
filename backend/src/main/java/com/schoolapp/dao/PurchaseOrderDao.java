package com.schoolapp.dao;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import com.schoolapp.entity.PurchaseOrder;

import com.schoolapp.repository.PurchaseOrderRepo;

@Component
public class PurchaseOrderDao {
	@Autowired
	PurchaseOrderRepo purchaseOrderRepo;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	int existInqMst = 0;

	public List<PurchaseOrder> savePurchaseOrder(List<PurchaseOrder> purchaseOrder) {
		for (PurchaseOrder al2 : purchaseOrder) {
			int srValide = al2.getPurchaseOrderMstId();
			if (srValide == 0) {
				java.sql.Date purchaseDate = (java.sql.Date) al2.getPurchaseDate();
				int customerId = al2.getCustomerId();
				int userId = al2.getUserId();
				int orgId = al2.getOrgId();
				int branchId = al2.getBranchId();
				java.sql.Date createdDate = (java.sql.Date) al2.getCreatedDate();
				int updatedBy = al2.getUpdatedBy();
				java.sql.Date updatedDate = (java.sql.Date) al2.getUpdatedDate();
				int isActive = al2.getIsActive();
				int srNo = 0;
				int total = 0;

				String sql = "INSERT INTO purchase_order_master( date, customer_id, branch_id, created_date, is_active, org_id, updated_by, updated_date, user_id,srno,total) "
						+ "VALUES (?,?, ?, ?, ?, ?, ?, ?, ?,?,?)";

				KeyHolder keyHolder = new GeneratedKeyHolder();
				jdbcTemplate.update(connection -> {
					PreparedStatement pst = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
					pst.setDate(1, purchaseDate);
					pst.setInt(2, customerId);
					pst.setInt(3, branchId);
					pst.setDate(4, createdDate);
					pst.setInt(5, isActive);
					pst.setInt(6, orgId);
					pst.setInt(7, updatedBy);
					pst.setDate(8, updatedDate);
					pst.setInt(9, userId);
					pst.setInt(10, srNo);
					pst.setInt(11, total);
					return pst;
				}, keyHolder);

				int purchaseOrderId = keyHolder.getKey().intValue();
				for (PurchaseOrder al3 : purchaseOrder) {
					al3.setPurchaseOrderMstId(purchaseOrderId);
					existInqMst = purchaseOrderId;
				}
			}
			System.out.println("Data inserted successfully...");
			return purchaseOrderRepo.saveAll(purchaseOrder);
		}
		return purchaseOrder;
	}

	public PurchaseOrder updateDeletePurchaseOrder(PurchaseOrder purchaseOrder) {
		Integer purchaseOrderId = purchaseOrder.getPurchaseOrderId();
		int updatedBy = purchaseOrder.getUpdatedBy();
		java.util.Date updatedDate = purchaseOrder.getUpdatedDate();
		int orgId = purchaseOrder.getOrgId();
		int isActive = purchaseOrder.getIsActive();

		String query = "update purchase_detailes set is_active=?, updated_by=?,updated_date=? "
				+ "where purchase_detailes_id=? and org_id=?";
		jdbcTemplate.update(query, isActive, updatedBy, updatedDate, purchaseOrderId, orgId);
		System.out.println("Record updated");
		return purchaseOrderRepo.save(purchaseOrder);
	}

	public List<PurchaseOrder> getAllPurchaseOrder() {
		return purchaseOrderRepo.findAll();
	}

	public PurchaseOrder findPurchaseOrderById(int PurchaseOrderId) {
		return (PurchaseOrder) purchaseOrderRepo.findById(PurchaseOrderId).get();
	}

	public String deletePurchaseOrderById(int PurchaseOrderId) {
		purchaseOrderRepo.deleteById(PurchaseOrderId);
		return "deleted";
	}
}

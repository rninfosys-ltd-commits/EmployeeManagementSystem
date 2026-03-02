package com.schoolapp.dao;

import java.sql.Date;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.schoolapp.entity.WorkOrderMaster;
import com.schoolapp.repository.WorkOrderMasterRepo;

@Component
public class WorkOrderMasterDao {
	@Autowired
	WorkOrderMasterRepo workOrderMasterRepo;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public WorkOrderMaster saveWorkOrderMaster(WorkOrderMaster workOrderMaster) {
		return workOrderMasterRepo.save(workOrderMaster);
	}

	public String getAllWorkOrderMaster() {
		String sql = "SELECT wm.work_order_master_id, wm.branch_id, wm.contractor_id, wm.created_date, wm.is_active, wm.order_date, wm.org_id, wm.product_id, wm.sr_no, wm.updated_by, wm.updated_date, wm.user_id, b.branch_name, c.contractor_name, p.name as product_name, u.user_name "
				+ "FROM crmdb.work_order_master wm "
				+ "inner join contractor c on wm.contractor_id=c.contractor_id and wm.org_id=c.org_id "
				+ "inner join branch b on wm.branch_id=b.branch_id "
				+ "inner join product p on wm.product_id=p.product_id "
				+ "inner join user u on wm.user_id=u.u_id";
		List<Map<String, Object>> result = jdbcTemplate.queryForList(sql);
		return new JSONArray(result).toString();
	}

	public String updateWorkOrderMaster(WorkOrderMaster workOrderMaster) {
		Integer workOrderMasterId = workOrderMaster.getWorkOrderMasterId();

		int branchId = workOrderMaster.getBranchId();
		int contractorId = workOrderMaster.getContractorId();
		Date createdDate = workOrderMaster.getCreatedDate();
		int isActive = workOrderMaster.getIsActive();
		Date orderDate = workOrderMaster.getOrderDate();
		int orgId = workOrderMaster.getOrgId();
		int productId = workOrderMaster.getProductId();
		int srNo = workOrderMaster.getSrNo();
		int updatedBy = workOrderMaster.getUserId();
		Date updatedDate = workOrderMaster.getUpdatedDate();
		int userId = workOrderMaster.getUserId();

		String query = "update work_order_master set branch_id = ?, contractor_id = ?, created_date = ?, "
				+ "is_active = ?, order_date = ?, product_id = ?, sr_no = ?, updated_by = ?, updated_date = ?, user_id=? "
				+ "where work_order_master_id = ? and org_id = ?";

		jdbcTemplate.update(query, branchId, contractorId, createdDate, isActive, orderDate, productId, srNo, updatedBy,
				updatedDate, userId, workOrderMasterId, orgId);

		System.out.println("Record updated successfully..!");
		return "Record updated..!";

	}

	public WorkOrderMaster updateDeleteWorkOrderMaster(WorkOrderMaster workOrderMaster) {
		Integer workOrderMasterId = workOrderMaster.getWorkOrderMasterId();

		int updatedBy = workOrderMaster.getUpdatedBy();
		java.util.Date updatedDate = workOrderMaster.getUpdatedDate();
		int orgId = workOrderMaster.getOrgId();
		int isActive = workOrderMaster.getIsActive();

		String query = "update work_order_master set is_active=?, updated_by=?,updated_date=? "
				+ "where work_order_master_id=? and org_id=?";
		jdbcTemplate.update(query, isActive, updatedBy, updatedDate, workOrderMasterId, orgId);
		return workOrderMasterRepo.save(workOrderMaster);
	}

	public WorkOrderMaster findWorkOrderMasterById(int WorkOrderMasterId) {
		return (WorkOrderMaster) workOrderMasterRepo.findById(WorkOrderMasterId).get();
	}

	public String deleteWorkOrderMasterById(int WorkOrderMasterId) {
		workOrderMasterRepo.deleteById(WorkOrderMasterId);
		return "deleted";
	}
}

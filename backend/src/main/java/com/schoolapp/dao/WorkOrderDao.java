package com.schoolapp.dao;

import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import com.schoolapp.entity.WorkOrder;
import com.schoolapp.repository.WorkOrderRepo;

@Component
public class WorkOrderDao {
	@Autowired
	WorkOrderRepo workOrderRepo;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public List<WorkOrder> saveWorkOrder(List<WorkOrder> workOrder) {
		if (workOrder.isEmpty())
			return workOrder;

		WorkOrder first = workOrder.get(0);
		String sql = "INSERT INTO work_order_master(branch_id, contractor_id, created_date, is_active, order_date, org_id, product_id, updated_by, updated_date, user_id, sr_no) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(connection -> {
			java.sql.PreparedStatement ps = connection.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS);
			ps.setInt(1, first.getBranchId());
			ps.setInt(2, first.getContractorId());
			ps.setDate(3, new java.sql.Date(first.getCreatedDate().getTime()));
			ps.setInt(4, first.getIsActive());
			ps.setDate(5, new java.sql.Date(first.getOrderDate().getTime()));
			ps.setInt(6, first.getOrgId());
			ps.setInt(7, first.getProductId());
			ps.setInt(8, first.getUpdatedBy());
			ps.setDate(9, new java.sql.Date(first.getUpdatedDate().getTime()));
			ps.setInt(10, first.getUserId());
			ps.setInt(11, first.getSrNo());
			return ps;
		}, keyHolder);

		int workOrderId = keyHolder.getKey().intValue();

		for (WorkOrder al : workOrder) {
			al.setSrNo(workOrderId);
		}

		return (List<WorkOrder>) workOrderRepo.saveAll(workOrder);
	}

	public String getAllWorkOrder() {
		String sql = "SELECT wo.work_order_id,wo.area_id,wo.area_sqr_ft,wo.branch_id,wo.contractor_id,wo.created_date,wo.is_active,"
				+ " wo.order_date,wo.org_id,wo.product_id,wo.rate,wo.sr_no,wo.updated_by,wo.updated_date,wo.user_id,"
				+ " c.contractor_name,o.org_name,p.name,am.area_master_name,b.branch_name,u.user_name"
				+ " FROM work_order wo"
				+ " INNER join work_order_master wm on wo.sr_no=wm.work_order_master_id"
				+ " INNER join contractor c on wm.contractor_id=c.contractor_id"
				+ " INNER JOIN organization o ON wo.org_id = o.org_id "
				+ " INNER join product p on wo.product_id=p.product_id "
				+ " INNER join area_master am on wo.area_id=am.area_master_id"
				+ " INNER JOIN branch b ON wo.branch_id = b.branch_id "
				+ " INNER JOIN user u ON wo.user_id = u.u_id";

		List<Map<String, Object>> result = jdbcTemplate.queryForList(sql);
		return new JSONArray(result).toString();
	}

	// ----------------------------------------------------

	public String getAllWorkOrderByOrderId(int id) {
		String sql = "SELECT wo.work_order_id,wo.area_id,wo.area_sqr_ft,wo.branch_id,wo.contractor_id,wo.created_date,wo.is_active,"
				+ " wo.order_date,wo.org_id,wo.product_id,wo.rate,wo.sr_no,wo.updated_by,wo.updated_date,wo.user_id,"
				+ " c.contractor_name,o.org_name,p.name,am.area_master_name,b.branch_name,u.user_name"
				+ " FROM work_order wo"
				+ " INNER join work_order_master wm on wo.sr_no=wm.work_order_master_id"
				+ " INNER join contractor c on wm.contractor_id=c.contractor_id"
				+ " INNER JOIN organization o ON wo.org_id = o.org_id "
				+ " INNER join product p on wo.product_id=p.product_id "
				+ " INNER join area_master am on wo.area_id=am.area_master_id"
				+ " INNER JOIN branch b ON wo.branch_id = b.branch_id "
				+ " INNER JOIN user u ON wo.user_id = u.u_id WHERE wo.sr_no = ?";

		List<Map<String, Object>> result = jdbcTemplate.queryForList(sql, id);
		return new JSONArray(result).toString();
	}

	public String getWorkOrderContractor() {
		String sql = "SELECT wo.contractor_id, c.contractor_name FROM contractor c"
				+ " INNER join work_order_master wm on wm.contractor_id=c.contractor_id"
				+ " INNER JOIN work_order wo ON wo.org_id = wm.org_id AND wm.contractor_id=wo.contractor_id"
				+ " WHERE wo.org_id=wm.org_id AND wo.contractor_id=c.contractor_id"
				+ " GROUP BY wo.contractor_id, c.contractor_name";

		List<Map<String, Object>> result = jdbcTemplate.queryForList(sql);
		return new JSONArray(result).toString();
	}

	public String getWorkOrderProduct(WorkOrder workOrder) {
		int contractorId = workOrder.getContractorId();
		String sql = "SELECT p.product_id, concat(p.name, ' woNo => ', wo.work_order_id, ' - ', am.area_master_name) as name,"
				+ " wo.area_id, wo.work_order_id, wo.sr_no FROM product p"
				+ " INNER join work_order_master wm on wm.product_id=p.product_id"
				+ " INNER JOIN work_order wo ON wo.org_id = wm.org_id AND wm.product_id=wo.product_id"
				+ " INNER join area_master am on wo.area_id=am.area_master_id"
				+ " WHERE wo.org_id=wm.org_id AND wo.product_id=p.product_id AND wm.contractor_id=?"
				+ " GROUP BY p.product_id, p.name, wo.work_order_id, am.area_master_name, wo.area_id, wo.sr_no"
				+ " ORDER BY wo.sr_no DESC";

		List<Map<String, Object>> result = jdbcTemplate.queryForList(sql, contractorId);
		return new JSONArray(result).toString();
	}

	public String getWorkOrderArea(WorkOrder workOrder) {
		int productId = workOrder.getProductId();
		int contractorId = workOrder.getContractorId();
		int workOrderId = workOrder.getWorkOrderId();
		String sql = "SELECT wo.area_id, am.area_master_name, wo.sr_no, wo.work_order_id FROM area_master am"
				+ " INNER JOIN work_order wo ON wo.org_id = am.org_id AND am.area_master_id=wo.area_id"
				+ " INNER join product p on wo.product_id=p.product_id"
				+ " WHERE p.product_id=? AND wo.contractor_id=? AND wo.work_order_id=?"
				+ " GROUP BY am.area_master_id, am.area_master_name, wo.sr_no, wo.work_order_id";

		List<Map<String, Object>> result = jdbcTemplate.queryForList(sql, productId, contractorId, workOrderId);
		return new JSONArray(result).toString();
	}

	public String updateWorkOrder(WorkOrder workOrder) {
		Integer workOrderId = workOrder.getWorkOrderId();
		int areaSqrFt = workOrder.getAreaSqrFt();
		java.sql.Date createdDate = workOrder.getCreatedDate();
		int isActive = workOrder.getIsActive();
		int orgId = workOrder.getOrgId();
		int rate = workOrder.getRate();
		int updatedBy = workOrder.getUserId();

		String sql = "UPDATE work_order SET area_sqr_ft = ?, rate = ?, created_date = ?, is_active = ?, updated_by = ? "
				+ "WHERE work_order_id = ? AND org_id = ?";

		jdbcTemplate.update(sql, areaSqrFt, rate, createdDate, isActive, updatedBy, workOrderId, orgId);

		System.out.println("Record updated successfully..!");
		return "Record updated..!";
	}

	public WorkOrder updateDeleteWorkOrder(WorkOrder workOrder) {
		Integer workOrderId = workOrder.getWorkOrderId();
		int updatedBy = workOrder.getUpdatedBy();
		java.sql.Date updatedDate = workOrder.getUpdatedDate();
		int orgId = workOrder.getOrgId();
		int isActive = workOrder.getIsActive();

		String sql = "UPDATE work_order SET is_active = ?, updated_by = ?, updated_date = ? "
				+ "WHERE work_order_id = ? AND org_id = ?";

		jdbcTemplate.update(sql, isActive, updatedBy, updatedDate, workOrderId, orgId);

		System.out.println("Record updated");
		return workOrderRepo.save(workOrder);
	}

	public WorkOrder findWorkOrderById(int WorkOrderId) {
		return (WorkOrder) workOrderRepo.findById(WorkOrderId).get();
	}

	public String deleteWorkOrderById(int WorkOrderId) {
		workOrderRepo.deleteById(WorkOrderId);
		return "deleted";
	}
}

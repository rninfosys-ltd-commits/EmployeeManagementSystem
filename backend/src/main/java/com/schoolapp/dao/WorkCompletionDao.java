package com.schoolapp.dao;

// import java.sql.Date;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Component;

// import com.schoolapp.entity.AccessPermission;
// import com.schoolapp.entity.LeadAccounts;
// import com.schoolapp.entity.User;
// import com.schoolapp.entity.Wing;
import com.schoolapp.entity.WorkCompletion;
// import com.schoolapp.entity.WorkOrder;
import com.schoolapp.repository.WorkCompletionRepo;
// import com.schoolapp.repository.WorkOrderRepo;

@Component
public class WorkCompletionDao {
	@Autowired
	WorkCompletionRepo workCompletionRepo;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public WorkCompletion saveWorkCompletion(WorkCompletion workCompletion) {
		return workCompletionRepo.save(workCompletion);
	}

	public String getAllWorkCompletion() {
		String sql = "SELECT wc.contractor_id, wc.area_id, wc.work_master_id, wo.work_order_id, c.contractor_name, p.name, wo.area_sqr_ft, sum(wc.complete_sqr_ft) as total_complete_sq_ft, "
				+ "wo.area_sqr_ft - sum(wc.complete_sqr_ft) as pending_sq_ft, am.area_master_name "
				+ "FROM crmdb.work_completion wc "
				+ "inner join work_order wo on wc.work_master_id=wo.sr_no and wc.product_id=wo.product_id and wc.work_order_id=wo.work_order_id "
				+ "inner join product p on wo.product_id=p.product_id "
				+ "inner join area_master am on wc.area_id=am.area_master_id "
				+ "inner join contractor c on wo.contractor_id=c.contractor_id and wo.org_id=c.org_id "
				+ "group by wc.contractor_id, wc.area_id, wc.work_master_id, p.name, c.contractor_name, wo.work_order_id, wo.area_sqr_ft";
		List<Map<String, Object>> result = jdbcTemplate.queryForList(sql);
		return new JSONArray(result).toString();
	}

	public WorkCompletion updateDeleteWorkCompletion(WorkCompletion workCompletion) {
		Integer workCompletionId = workCompletion.getWorkCompletionId();
		int updatedBy = workCompletion.getUpdatedBy();
		java.util.Date updatedDate = workCompletion.getUpdatedDate();
		int orgId = workCompletion.getOrgId();
		int isActive = workCompletion.getIsActive();

		String query = "update work_completion set is_active=?, updated_by=?,updated_date=? "
				+ "where work_completion_id=? and org_id=?";
		jdbcTemplate.update(query, isActive, updatedBy, updatedDate, workCompletionId, orgId);
		return workCompletionRepo.save(workCompletion);
	}

	public WorkCompletion findWorkCompletionById(int WorkCompletionId) {
		return (WorkCompletion) workCompletionRepo.findById(WorkCompletionId).get();
	}

	public String deleteWorkCompletionById(int WorkCompletionId) {
		workCompletionRepo.deleteById(WorkCompletionId);
		return "deleted";
	}
}

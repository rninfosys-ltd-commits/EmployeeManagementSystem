package com.schoolapp.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.schoolapp.entity.InqueryMaster;
import com.schoolapp.repository.InqueryMasterRepo;

@Component
public class InqueryMasterDao {

	@Autowired
	InqueryMasterRepo inqueryMasterRepo;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public InqueryMaster saveInqueryMaster(InqueryMaster inqueryMaster) {
		System.out.println("Data inserted successfully...");
		return inqueryMasterRepo.save(inqueryMaster);
	}

	public InqueryMaster updateDeleteInqueryMaster(InqueryMaster inqueryMaster) {
		Integer inqueryMasterId = inqueryMaster.getInqueryId();
		int updatedBy = inqueryMaster.getUpdatedBy();
		java.util.Date updatedDate = inqueryMaster.getUpdatedDate();
		int orgId = inqueryMaster.getOrgId();
		int isActive = inqueryMaster.getIsActive();

		String query = "update inquery_master set is_active=?, updated_by=?,updated_date=? where inquery_id=? and org_id=?";
		jdbcTemplate.update(query, isActive, updatedBy, updatedDate, inqueryMasterId, orgId);
		System.out.println("Record updated");
		return inqueryMasterRepo.save(inqueryMaster);
	}

	public List<InqueryMaster> getAllInquery() {
		return inqueryMasterRepo.findAll();
	}

	public InqueryMaster findInqueryById(int InqId) {
		return inqueryMasterRepo.findById(InqId).get();
	}

	public String deleteInqueryByID(int InqId) {
		inqueryMasterRepo.deleteById(InqId);
		return "deleted";
	}

}

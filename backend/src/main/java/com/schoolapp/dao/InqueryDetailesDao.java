package com.schoolapp.dao;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import com.schoolapp.entity.InqueryDetailes;
import com.schoolapp.repository.InqueryDetailesRepo;

@Component
public class InqueryDetailesDao {
	@Autowired
	InqueryDetailesRepo inqueryDetailesRepo;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	int existInqMst = 0;

	public InqueryDetailes saveInqueryDetailes(InqueryDetailes inqueryDetailes) {
		int srValide = inqueryDetailes.getInqueryMstNo();

		if (srValide > 0) {
			java.sql.Date inqueryDate = (java.sql.Date) inqueryDetailes.getInqueryDate();
			int leadAccountId = inqueryDetailes.getLeadAccountId();
			int userId = inqueryDetailes.getUserId();
			int orgId = inqueryDetailes.getOrgId();
			int branchId = inqueryDetailes.getBranchId();
			java.sql.Date createdDate = (java.sql.Date) inqueryDetailes.getCreatedDate();
			int updatedBy = inqueryDetailes.getUpdatedBy();
			java.sql.Date updatedDate = (java.sql.Date) inqueryDetailes.getUpdatedDate();
			int isActive = inqueryDetailes.getIsActive();
			int srNo = 0;
			int total = 0;

			String sql = "INSERT INTO inquery_master( date, lead_account, branch_id, created_date, is_active, org_id,   updated_by, updated_date, user_id,srno,total) "
					+ "VALUES (?,?, ?, ?, ?, ?, ?, ?, ?,?,?)";

			KeyHolder keyHolder = new GeneratedKeyHolder();
			jdbcTemplate.update(connection -> {
				PreparedStatement pst = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				pst.setDate(1, inqueryDate);
				pst.setInt(2, leadAccountId);
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

			int inqueryDetailesId = keyHolder.getKey().intValue();
			System.out.println("Inserted inqueryDetailesId: " + inqueryDetailesId);
			inqueryDetailes.setInqueryMstNo(inqueryDetailesId);
			existInqMst = inqueryDetailesId;
		}
		System.out.println("Data inserted successfully...");
		return inqueryDetailesRepo.save(inqueryDetailes);
	}

	public InqueryDetailes updateDeleteInqueryDetailes(InqueryDetailes inqueryDetailes) {
		Integer inqueryDetailesId = inqueryDetailes.getInqueryDetailesId();

		int updatedBy = inqueryDetailes.getUpdatedBy();
		java.util.Date updatedDate = inqueryDetailes.getUpdatedDate();
		int orgId = inqueryDetailes.getOrgId();
		int isActive = inqueryDetailes.getIsActive();

		String query = "update inquery_detailes set is_active=?, updated_by=?,updated_date=? "
				+ "where inquery_detailes_id=? and org_id=?";
		jdbcTemplate.update(query, isActive, updatedBy, updatedDate, inqueryDetailesId, orgId);
		System.out.println("Record updated");
		return inqueryDetailesRepo.save(inqueryDetailes);
	}

	public List<InqueryDetailes> getAllInqueryDetailes() {
		return inqueryDetailesRepo.findAll();
	}

	public InqueryDetailes findInqueryDetailesById(int InqueryDetailesId) {
		return (InqueryDetailes) inqueryDetailesRepo.findById(InqueryDetailesId).get();
	}

	public String deleteInqueryDetailesById(int InqueryDetailesId) {
		inqueryDetailesRepo.deleteById(InqueryDetailesId);
		return "deleted";
	}
}

package com.schoolapp.dao;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import com.schoolapp.entity.Quotation;
import com.schoolapp.repository.QuotationRepo;

@Component
public class QuotationDao {

	@Autowired
	QuotationRepo quotationRepo;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	int existInqMst = 0;

	public List<Quotation> saveQuotation(List<Quotation> quotation) {
		for (Quotation al2 : quotation) {
			int srValide = al2.getQuotationMstId();
			if (srValide == 0) {
				java.sql.Date quotationDate = (java.sql.Date) al2.getQuotationDate();
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

				String sql = "INSERT INTO quotation_master( date, customer_id, branch_id, created_date, is_active, org_id, updated_by, updated_date, user_id,srno,total) "
						+ "VALUES (?,?, ?, ?, ?, ?, ?, ?, ?,?,?)";

				KeyHolder keyHolder = new GeneratedKeyHolder();
				jdbcTemplate.update(connection -> {
					PreparedStatement pst = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
					pst.setDate(1, quotationDate);
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

				int quotationId = keyHolder.getKey().intValue();
				for (Quotation al3 : quotation) {
					al3.setQuotationMstId(quotationId);
					existInqMst = quotationId;
				}
			}
			System.out.println("Data inserted successfully...");
			return quotationRepo.saveAll(quotation);
		}
		return quotation;
	}

	public Quotation updateDeleteQuotation(Quotation quotation) {
		Integer quotationId = quotation.getQuotationId();
		int updatedBy = quotation.getUpdatedBy();
		java.util.Date updatedDate = quotation.getUpdatedDate();
		int orgId = quotation.getOrgId();
		int isActive = quotation.getIsActive();

		String query = "update quotation_detailes set is_active=?, updated_by=?,updated_date=? "
				+ "where quotation_detailes_id=? and org_id=?";
		jdbcTemplate.update(query, isActive, updatedBy, updatedDate, quotationId, orgId);
		System.out.println("Record updated");
		return quotationRepo.save(quotation);
	}

	public List<Quotation> getAllQuotation() {
		return quotationRepo.findAll();
	}

	public Quotation findQuotationById(int QuotationId) {
		return (Quotation) quotationRepo.findById(QuotationId).get();
	}

	public String deleteQuotationById(int QuotationId) {
		quotationRepo.deleteById(QuotationId);
		return "deleted";
	}
}

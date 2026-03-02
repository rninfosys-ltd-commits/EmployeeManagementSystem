package com.schoolapp.dao;

import java.sql.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.schoolapp.entity.Contractor;
import com.schoolapp.repository.ContractorRepo;

@Component
public class ContractorDao {
	@Autowired
	ContractorRepo contractorRepo;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public Contractor saveContractor(Contractor contractor) {
		System.out.println("Data inserted successfully...");
		return contractorRepo.save(contractor);
	}

	public List<Contractor> getAllContractor() {
		return contractorRepo.findAll();
	}

	public Contractor findContractorById(int ContractorId) {

		return contractorRepo.findById(ContractorId).get();
	}

	public String updateContractor(Contractor contractor) {
		Integer contractorId = contractor.getContractorId();
		int userId = contractor.getUserId();
		Date date = contractor.getDate();
		String ownerName = contractor.getOwnerName();
		String contractorName = contractor.getContractorName();
		int ownerContact = contractor.getOwnerContact();
		String panNo = contractor.getPanNo();
		String gstNo = contractor.getGstNo();
		String email = contractor.getEmail();
		String website = contractor.getWebsite();
		int phone = contractor.getPhone();
		int fax = contractor.getFax();
		String invoiceAddress = contractor.getInvoiceAddress();
		int income = contractor.getIncome();
		int otherIncome = contractor.getOtherIncome();
		int budget1 = contractor.getBudget1();
		int budget2 = contractor.getBudget2();
		String notes = contractor.getNotes();
		int isActive = contractor.getIsActive();
		int orgId = contractor.getOrgId();
		int branchId = contractor.getBranchId();
		int updatedBy = contractor.getUpdatedBy();
		Date updatedDate = contractor.getUpdatedDate();

		String query = "update contractor set budget1=?, budget2=?, contractor_name=?, created_date=?, date=?, "
				+ "email=?, fax=?, gst_no=?, income=?, invoice_address=?, is_active=?, notes=?, other_income=?,"
				+ "owner_contact=?, owner_name=?, pan_no=?, phone=?, updated_by=?, updated_date=?, user_id=?,"
				+ "website=?, branch_id=? where contractor_id=? and org_id=?";

		jdbcTemplate.update(query, budget1, budget2, contractorName, date, date, email, fax, gstNo, income,
				invoiceAddress, isActive, notes, otherIncome, ownerContact, ownerName, panNo, phone, updatedBy,
				updatedDate, userId, website, branchId, contractorId, orgId);

		System.out.println("Record updated successfully..!");
		return "Record updated..!";
	}

	public Contractor updateDeleteContractor(Contractor contractor) {
		Integer contractorId = contractor.getContractorId();
		int updatedBy = contractor.getUpdatedBy();
		Date updatedDate = contractor.getUpdatedDate();
		int orgId = contractor.getOrgId();
		int isActive = contractor.getIsActive();

		String query = "update contractor set is_active=?, updated_by=?,updated_date=? where contractor_id=? and org_id=?";
		jdbcTemplate.update(query, isActive, updatedBy, updatedDate, contractorId, orgId);
		System.out.println("Record updated");
		return contractorRepo.save(contractor);
	}

	public String deleteContractorById(int ContractorId) {
		contractorRepo.deleteById(ContractorId);
		return "deleted";
	}

}

package com.schoolapp.dao;

import java.sql.Date;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.schoolapp.entity.Branch;
import com.schoolapp.repository.BranchRepo;

@Component
public class BranchDao {
	@Autowired
	BranchRepo branchRepo;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public Branch saveBranch(Branch branch) {
		System.out.println("Data inserted successfully...");
		return branchRepo.save(branch);
	}

	public Branch updateDeleteBranch(Branch branch) {

		Integer branchId = branch.getBranchId();
		int updatedBy = branch.getUpdatedBy();
		Date updatedDate = (Date) branch.getUpdatedDate();
		int orgId = branch.getOrgId();
		int isActive = branch.getIsActive();

		String query = "update branch set is_active=?, updated_by=?,updated_date=? where branch_id=? and org_id=?";
		jdbcTemplate.update(query, isActive, updatedBy, updatedDate, branchId, orgId);
		System.out.println("Record updated");
		return branchRepo.save(branch);
	}

	public List<Branch> getAllBranch() {
		return branchRepo.findAll();
	}

	public Branch findBranchById(int branchId) {
		return branchRepo.findById(branchId).get();
	}

	public String deleteBranchById(int branchId) {
		branchRepo.deleteById(branchId);
		return "deleted";
	}

}

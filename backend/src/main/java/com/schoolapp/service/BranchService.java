package com.schoolapp.service;

// import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.schoolapp.dao.BranchDao;
import com.schoolapp.dao.UserDao;
import com.schoolapp.entity.AccessPermission;
import com.schoolapp.entity.Branch;
import com.schoolapp.entity.User;

@Service
public class BranchService {
	@Autowired
	BranchDao branchDao;

	public Branch saveBranch(Branch branch) {
		Branch path = null;
		int userId = branch.getUserId();

		User users = new User();
		users.setuId(userId);

		AccessPermission accessPermission = new AccessPermission();
		accessPermission.setAccessUserId(userId);

		List<Object> al = new ArrayList<>();

		UserDao userDao = new UserDao();

		al.add(userDao.userValidation(users, accessPermission));

		int valideSave = accessPermission.getAccessCreate();
		if (valideSave == 1) {
			path = branchDao.saveBranch(branch);

		} else {
			System.out.println("Invalide Credantials");
		}
		return path;

	}

	public List<Branch> getAllBranch() {
		return branchDao.getAllBranch();
	}

	public Branch updateDeleteBranch(Branch branch) {

		Branch path = null;
		int userId = branch.getUserId();

		User users = new User();
		users.setuId(userId);

		AccessPermission accessPermission = new AccessPermission();
		accessPermission.setAccessUserId(userId);

		List<Object> al = new ArrayList<>();

		UserDao userDao = new UserDao();

		al.add(userDao.userValidation(users, accessPermission));

		int valideSave = accessPermission.getAccessUpdate();
		if (valideSave == 1) {
			path = branchDao.updateDeleteBranch(branch);

		} else {
			System.out.println("Invalide Credantials");
		}
		return path;

	}

	public Branch findBranchById(int branchId) {
		return branchDao.findBranchById(branchId);
	}

	public Branch updateBranch(Branch branch) {
		Branch br = branchDao.findBranchById(branch.getBranchId());
		br.setBranchId(branch.getBranchId());
		br.setSerialNumber(branch.getSerialNumber());
		br.setBranchName(branch.getBranchName());
		br.setOrgId(branch.getOrgId());
		br.setDate(branch.getDate());
		br.setBranchManagerName(branch.getBranchManagerName());
		br.setBranchContact(branch.getBranchContact());
		br.setPanNo(branch.getPanNo());
		br.setGstNo(branch.getGstNo());
		br.setEmail(branch.getEmail());
		br.setWebsite(branch.getWebsite());
		br.setPhone(branch.getPhone());
		br.setFax(branch.getFax());
		br.setInvoiceAddress(branch.getInvoiceAddress());
		br.setIncome(branch.getIncome());
		br.setIncomeSource(branch.getIncomeSource());
		br.setOtherIncome(branch.getOtherIncome());
		br.setOtherIncomeSource(branch.getOtherIncomeSource());
		br.setBudget1(branch.getBudget1());
		br.setBudget2(branch.getBudget2());
		br.setNotes(branch.getNotes());
		br.setIsActive(branch.getIsActive());
		br.setStateId(branch.getStateId());
		br.setDistId(branch.getDistId());
		br.setCityId(branch.getCityId());
		br.setUserId(branch.getUserId());
		br.setCreatedDate(branch.getCreatedDate());
		br.setUpdatedBy(branch.getUpdatedBy());
		br.setUpdatedDate(branch.getUpdatedDate());

		return branchDao.saveBranch(br);
	}

	public String deleteBranchById(int BranchId) {
		branchDao.deleteBranchById(BranchId);
		return "deleted";
	}
}

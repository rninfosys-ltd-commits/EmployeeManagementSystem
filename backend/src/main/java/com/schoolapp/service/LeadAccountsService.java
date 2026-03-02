package com.schoolapp.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.schoolapp.dao.LeadAccountsDao;
import com.schoolapp.dao.UserDao;
import com.schoolapp.entity.AccessPermission;
import com.schoolapp.entity.LeadAccounts;
import com.schoolapp.entity.User;

@Service
public class LeadAccountsService {

	@Autowired
	LeadAccountsDao leadAccountsDao;

	public LeadAccounts saveLeadAccounts(LeadAccounts leadAccounts) throws ClassNotFoundException, SQLException {
		LeadAccounts path = null;
		int userId = leadAccounts.getUserId();

		User users = new User();
		users.setuId(userId);

		AccessPermission accessPermission = new AccessPermission();
		accessPermission.setAccessUserId(userId);

		List<Object> al = new ArrayList<>();

		UserDao userDao = new UserDao();

		al.add(userDao.userValidation(users, accessPermission));

		int valideSave = accessPermission.getAccessCreate();
		if (valideSave == 1) {
			path = leadAccountsDao.saveLeadAccounts(leadAccounts);

		} else {
			System.out.println("Invalide Credantials");
		}
		return path;

	}

	public String getAllLeadAccounts() throws Exception {
		return leadAccountsDao.getAllLeadAccounts();
	}

	public LeadAccounts findLeadAccountsById(int LeadAccuntId) {
		return leadAccountsDao.findLeadAccountsById(LeadAccuntId);
	}

	public LeadAccounts updateDeleteLeadAccounts(LeadAccounts leadAccounts)
			throws ClassNotFoundException, SQLException {
		LeadAccounts path = null;
		int userId = leadAccounts.getUserId();

		User users = new User();
		users.setuId(userId);

		AccessPermission accessPermission = new AccessPermission();
		accessPermission.setAccessUserId(userId);

		List<Object> al = new ArrayList<>();

		UserDao userDao = new UserDao();

		al.add(userDao.userValidation(users, accessPermission));

		int valideSave = accessPermission.getAccessUpdate();

		if (valideSave == 1) {
			path = leadAccountsDao.updateDeleteLeadAccounts(leadAccounts);

		} else {
			System.out.println("Invalide Credantials");
		}
		return path;
	}

	public LeadAccounts updateLeadAccounts(LeadAccounts leadAccounts) throws ClassNotFoundException, SQLException {
		LeadAccounts accounts = leadAccountsDao.findLeadAccountsById(leadAccounts.getLeadId());
		accounts.setLeadId(leadAccounts.getLeadId());
		accounts.setDate(leadAccounts.getDate());
		accounts.setcName(leadAccounts.getcName());
		accounts.setOwnerName(leadAccounts.getOwnerName());
		accounts.setOwnerContact(leadAccounts.getOwnerContact());
		accounts.setPanNo(leadAccounts.getPanNo());
		accounts.setGstNo(leadAccounts.getGstNo());
		accounts.setEmail(leadAccounts.getEmail());
		accounts.setWebsite(leadAccounts.getWebsite());
		accounts.setPhone(leadAccounts.getPhone());
		accounts.setFax(leadAccounts.getFax());
		accounts.setInvoiceAddress(leadAccounts.getInvoiceAddress());
		accounts.setStateId(leadAccounts.getStateId());
		accounts.setDistId(leadAccounts.getDistId());
		accounts.setCityId(leadAccounts.getCityId());
		accounts.setUserId(leadAccounts.getUserId());
		accounts.setCreatedDate(leadAccounts.getCreatedDate());
		accounts.setUpdatedBy(leadAccounts.getUpdatedBy());
		accounts.setUpdatedDate(leadAccounts.getUpdatedDate());
		accounts.setBranchId(leadAccounts.getBranchId());
		accounts.setOrgId(leadAccounts.getOrgId());
		accounts.setIncome(leadAccounts.getIncome());
		accounts.setIncomeSource(leadAccounts.getIncomeSource());
		accounts.setOtherIncome(leadAccounts.getOtherIncome());
		accounts.setOtherIncomeSource(leadAccounts.getOtherIncomeSource());
		accounts.setBudget(leadAccounts.getBudget());
		accounts.setBudgetOne(leadAccounts.getBudgetOne());
		accounts.setBudgetTwo(leadAccounts.getBudgetTwo());
		accounts.setNotes(leadAccounts.getNotes());
		accounts.setArea(leadAccounts.getArea());
		accounts.setIsActive(leadAccounts.getIsActive());

		return leadAccountsDao.saveLeadAccounts(accounts);
	}

	public String deleteLeadAccountsById(int LeadId) {
		leadAccountsDao.deleteLeadAccountById(LeadId);
		return "deleted";
	}
}

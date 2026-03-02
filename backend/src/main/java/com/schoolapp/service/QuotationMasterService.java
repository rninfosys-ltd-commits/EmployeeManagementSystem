package com.schoolapp.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.schoolapp.dao.QuotationMasterDao;
import com.schoolapp.dao.UserDao;
import com.schoolapp.entity.AccessPermission;
import com.schoolapp.entity.QuotationMaster;
import com.schoolapp.entity.User;

@Service
public class QuotationMasterService {
	@Autowired
	QuotationMasterDao quotationMasterDao;

	public QuotationMaster saveQuotationMst(QuotationMaster quotationMaster) {
		QuotationMaster path = null;
		int userId = quotationMaster.getUserId();

		User users = new User();
		users.setuId(userId);

		AccessPermission accessPermission = new AccessPermission();
		accessPermission.setAccessUserId(userId);

		List<Object> al = new ArrayList<>();

		UserDao userDao = new UserDao();

		al.add(userDao.userValidation(users, accessPermission));

		int valideSave = accessPermission.getAccessCreate();
		if (valideSave == 1) {
			path = quotationMasterDao.saveQuotationMaster(quotationMaster);

		} else {
			System.out.println("Invalide Credantials");
		}
		return path;

	}

	public QuotationMaster updateDeleteQuotationMaster(QuotationMaster quotationMaster) {
		QuotationMaster path = null;
		int userId = quotationMaster.getUserId();

		User users = new User();
		users.setuId(userId);

		AccessPermission accessPermission = new AccessPermission();
		accessPermission.setAccessUserId(userId);

		List<Object> al = new ArrayList<>();

		UserDao userDao = new UserDao();

		al.add(userDao.userValidation(users, accessPermission));

		int valideSave = accessPermission.getAccessUpdate();

		if (valideSave == 1) {
			path = quotationMasterDao.updateDeleteQuotationMaster(quotationMaster);

		} else {
			System.out.println("Invalide Credantials");
		}
		return path;
	}

	public String getAllQuotationMst(QuotationMaster quotationMaster) {
		return quotationMasterDao.getAllQuotation(quotationMaster);
	}

	public QuotationMaster findQuotationById(int InqId) {

		return quotationMasterDao.findQuotationById(InqId);
	}

	public QuotationMaster updateQuotation(QuotationMaster quotationMaster) {

		return quotationMasterDao.saveQuotationMaster(quotationMaster);
	}

	public String deleteQuotationById(int InqId) {
		quotationMasterDao.deleteQuotationByID(InqId);
		return "deleted";
	}
}

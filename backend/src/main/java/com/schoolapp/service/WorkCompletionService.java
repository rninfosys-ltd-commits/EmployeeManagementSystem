package com.schoolapp.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.schoolapp.dao.UserDao;
import com.schoolapp.dao.WorkCompletionDao;
import com.schoolapp.entity.AccessPermission;
import com.schoolapp.entity.User;
import com.schoolapp.entity.WorkCompletion;

@Service
public class WorkCompletionService {
	@Autowired
	WorkCompletionDao workCompletionDao;

	public WorkCompletion saveWorkCompletion(WorkCompletion workCompletion)
			throws ClassNotFoundException, SQLException {

		WorkCompletion path = null;
		int userId = workCompletion.getUserId();

		User users = new User();
		users.setuId(userId);

		AccessPermission accessPermission = new AccessPermission();
		accessPermission.setAccessUserId(userId);

		List<Object> al = new ArrayList<>();

		UserDao userDao = new UserDao();

		al.add(userDao.userValidation(users, accessPermission));

		int valideSave = accessPermission.getAccessCreate();

		if (valideSave == 1) {
			path = workCompletionDao.saveWorkCompletion(workCompletion);

		} else {
			System.out.println("Invalide Credantials");
		}
		return path;
	}

	public String getAllWorkCompletion() throws Exception {
		return workCompletionDao.getAllWorkCompletion();
	}

	public WorkCompletion findWorkCompletionById(int WorkCompletionId) {
		return workCompletionDao.findWorkCompletionById(WorkCompletionId);
	}

	public WorkCompletion updateDeleteWorkCompletion(WorkCompletion workCompletion)
			throws ClassNotFoundException, SQLException {
		WorkCompletion path = null;
		int userId = workCompletion.getUserId();

		User users = new User();
		users.setuId(userId);

		AccessPermission accessPermission = new AccessPermission();
		accessPermission.setAccessUserId(userId);

		List<Object> al = new ArrayList<>();

		UserDao userDao = new UserDao();

		al.add(userDao.userValidation(users, accessPermission));

		int valideSave = accessPermission.getAccessUpdate();

		if (valideSave == 1) {
			path = workCompletionDao.updateDeleteWorkCompletion(workCompletion);

		} else {
			System.out.println("Invalide Credantials");
		}
		return path;
	}

	public WorkCompletion updateWorkCompletion(WorkCompletion workCompletion)
			throws ClassNotFoundException, SQLException {
		WorkCompletion accounts = workCompletionDao.findWorkCompletionById(workCompletion.getWorkCompletionId());
		accounts.setUserId(workCompletion.getUserId());
		accounts.setContractorId(workCompletion.getContractorId());
		accounts.setCreatedDate(workCompletion.getCreatedDate());
		accounts.setUpdatedBy(workCompletion.getUpdatedBy());
		accounts.setUpdatedDate(workCompletion.getUpdatedDate());
		accounts.setBranchId(workCompletion.getBranchId());
		accounts.setOrgId(workCompletion.getOrgId());
		accounts.setAreaId(workCompletion.getAreaId());
		accounts.setIsActive(workCompletion.getIsActive());

		return workCompletionDao.saveWorkCompletion(accounts);
	}

	public String deleteWorkCompletionById(int workCompletionId) {
		workCompletionDao.deleteWorkCompletionById(workCompletionId);
		return "deleted";
	}
}

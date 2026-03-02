package com.schoolapp.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.schoolapp.dao.UserDao;
import com.schoolapp.dao.WorkOrderMasterDao;
import com.schoolapp.entity.AccessPermission;
import com.schoolapp.entity.User;
import com.schoolapp.entity.WorkOrderMaster;

@Service
public class WorkOrderMasterService {
	@Autowired
	WorkOrderMasterDao workOrderMasterDao;

	public WorkOrderMaster saveWorkOrderMaster(WorkOrderMaster workOrderMaster) {
		WorkOrderMaster path = null;
		int userId = workOrderMaster.getUserId();

		User users = new User();
		users.setuId(userId);

		AccessPermission accessPermission = new AccessPermission();
		accessPermission.setAccessUserId(userId);

		List<Object> al = new ArrayList<>();

		UserDao userDao = new UserDao();

		al.add(userDao.userValidation(users, accessPermission));

		int valideSave = accessPermission.getAccessCreate();

		if (valideSave == 1) {
			path = workOrderMasterDao.saveWorkOrderMaster(workOrderMaster);

		} else {
			System.out.println("Invalide Credantials");
		}
		return path;
	}

	public String getAllWorkOrderMaster() {
		return workOrderMasterDao.getAllWorkOrderMaster();
	}

	public WorkOrderMaster findWorkOrderMasterById(int WorkOrderMasterId) {
		return workOrderMasterDao.findWorkOrderMasterById(WorkOrderMasterId);
	}

	public String updateWorkOrderMaster(WorkOrderMaster workOrderMaster) {
		String path = null;
		int userId = workOrderMaster.getUserId();

		User users = new User();
		users.setuId(userId);

		AccessPermission accessPermission = new AccessPermission();
		accessPermission.setAccessUserId(userId);

		List<Object> al = new ArrayList<>();

		UserDao userDao = new UserDao();

		al.add(userDao.userValidation(users, accessPermission));

		int valideSave = accessPermission.getAccessUpdate();
		// System.out.println(valideSave);

		if (valideSave == 1) {
			path = workOrderMasterDao.updateWorkOrderMaster(workOrderMaster);

		} else {
			System.out.println("Invalide Credantials");
		}
		return path;
	}

	public WorkOrderMaster updateDeleteWorkOrderMaster(WorkOrderMaster workOrderMaster) {
		WorkOrderMaster path = null;
		int userId = workOrderMaster.getUserId();

		User users = new User();
		users.setuId(userId);

		AccessPermission accessPermission = new AccessPermission();
		accessPermission.setAccessUserId(userId);

		List<Object> al = new ArrayList<>();

		UserDao userDao = new UserDao();

		al.add(userDao.userValidation(users, accessPermission));

		int valideSave = accessPermission.getAccessUpdate();
		// System.out.println(valideSave);

		if (valideSave == 1) {
			path = workOrderMasterDao.updateDeleteWorkOrderMaster(workOrderMaster);

		} else {
			System.out.println("Invalide Credantials");
		}
		return path;
	}

	public String deleteWorkOrderMasterById(int workOrderMasterId) {
		workOrderMasterDao.deleteWorkOrderMasterById(workOrderMasterId);
		return "deleted";
	}
}

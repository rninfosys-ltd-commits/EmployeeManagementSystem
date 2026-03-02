package com.schoolapp.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.schoolapp.dao.DispatchMasterDao;
import com.schoolapp.dao.UserDao;
import com.schoolapp.entity.AccessPermission;
import com.schoolapp.entity.DispatchMaster;
import com.schoolapp.entity.User;

@Service
public class DispatchMasterService {

	@Autowired
	DispatchMasterDao DispatchMasterDao;

	public DispatchMaster saveDispatchMaster(DispatchMaster DispatchMaster)
			throws ClassNotFoundException, SQLException {

		DispatchMaster path = null;
		int userId = DispatchMaster.getUserId();

		User users = new User();
		users.setuId(userId);

		AccessPermission accessPermission = new AccessPermission();
		accessPermission.setAccessUserId(userId);

		List<Object> al = new ArrayList<>();

		UserDao userDao = new UserDao();

		al.add(userDao.userValidation(users, accessPermission));

		int valideSave = accessPermission.getAccessCreate();
		if (valideSave == 1) {
			path = DispatchMasterDao.saveDispatchMaster(DispatchMaster);

		} else {
			System.out.println("Invalide Credantials");
		}
		return path;

	}

	public String getAllDispatchMaster(DispatchMaster DispatchMaster) throws Exception {

		String path = null;
		int userId = DispatchMaster.getUserId();

		User users = new User();
		users.setuId(userId);

		AccessPermission accessPermission = new AccessPermission();
		accessPermission.setAccessUserId(userId);
		List<Object> al = new ArrayList<>();

		UserDao userDao = new UserDao();

		al.add(userDao.userValidation(users, accessPermission));

		int valideSave = accessPermission.getAccessCreate();
		if (valideSave == 1) {
			path = DispatchMasterDao.getAllDispatchMaster(DispatchMaster);

		} else {
			System.out.println("Invalide Credantials");
		}
		return path;
	}

	public String getAllStudentClassWise(DispatchMaster DispatchMaster) throws Exception {

		String path = null;
		int userId = DispatchMaster.getUserId();

		User users = new User();
		users.setuId(userId);

		AccessPermission accessPermission = new AccessPermission();
		accessPermission.setAccessUserId(userId);
		List<Object> al = new ArrayList<>();

		UserDao userDao = new UserDao();

		al.add(userDao.userValidation(users, accessPermission));

		int valideSave = accessPermission.getAccessCreate();
		if (valideSave == 1) {
			path = DispatchMasterDao.getAllDispatchMaster(DispatchMaster);

		} else {
			System.out.println("Invalide Credantials");
		}
		return path;
	}

	public DispatchMaster findDispatchMasterById(int DispatchMasterID) {
		return DispatchMasterDao.findDispatchMasterById(DispatchMasterID);
	}

	public DispatchMaster updateDeleteDispatchMaster(DispatchMaster DispatchMaster)
			throws ClassNotFoundException, SQLException {

		return DispatchMasterDao.updateDeleteDispatchMaster(DispatchMaster);
	}

	public String deleteDispatchMasterById(int DispatchMasterID) {
		DispatchMasterDao.deleteDispatchMasterById(DispatchMasterID);
		return "deleted";
	}
}

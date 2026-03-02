package com.schoolapp.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.schoolapp.dao.OutWorkMaterialMasterDao;
import com.schoolapp.dao.UserDao;
import com.schoolapp.entity.AccessPermission;
import com.schoolapp.entity.OutWorkMaterialMaster;
import com.schoolapp.entity.User;

@Service
public class OutWorkMaterialMasterService {

	@Autowired
	OutWorkMaterialMasterDao OutWorkMaterialMasterDao;

	public OutWorkMaterialMaster saveOutWorkMaterialMaster(OutWorkMaterialMaster OutWorkMaterialMaster)
			throws ClassNotFoundException, SQLException {

		com.schoolapp.entity.OutWorkMaterialMaster path = null;
		int userId = OutWorkMaterialMaster.getUserId();

		User users = new User();
		users.setuId(userId);

		AccessPermission accessPermission = new AccessPermission();
		accessPermission.setAccessUserId(userId);

		List<Object> al = new ArrayList<>();

		UserDao userDao = new UserDao();

		al.add(userDao.userValidation(users, accessPermission));

		int valideSave = accessPermission.getAccessCreate();
		if (valideSave == 1) {
			path = OutWorkMaterialMasterDao.saveOutWorkMaterialMaster(OutWorkMaterialMaster);

		} else {
			System.out.println("Invalide Credantials");
		}
		return path;

	}

	public String getAllOutWorkMaterialMaster(OutWorkMaterialMaster OutWorkMaterialMaster) throws Exception {

		String path = null;
		int userId = OutWorkMaterialMaster.getUserId();

		User users = new User();
		users.setuId(userId);

		AccessPermission accessPermission = new AccessPermission();
		accessPermission.setAccessUserId(userId);
		List<Object> al = new ArrayList<>();

		UserDao userDao = new UserDao();

		al.add(userDao.userValidation(users, accessPermission));

		int valideSave = accessPermission.getAccessCreate();
		if (valideSave == 1) {
			path = OutWorkMaterialMasterDao.getAllOutWorkMaterialMaster(OutWorkMaterialMaster);

		} else {
			System.out.println("Invalide Credantials");
		}
		return path;
	}

	public String getAllStudentClassWise(OutWorkMaterialMaster OutWorkMaterialMaster) throws Exception {

		String path = null;
		int userId = OutWorkMaterialMaster.getUserId();

		User users = new User();
		users.setuId(userId);

		AccessPermission accessPermission = new AccessPermission();
		accessPermission.setAccessUserId(userId);
		List<Object> al = new ArrayList<>();

		UserDao userDao = new UserDao();

		al.add(userDao.userValidation(users, accessPermission));

		int valideSave = accessPermission.getAccessCreate();
		if (valideSave == 1) {
			path = OutWorkMaterialMasterDao.getAllOutWorkMaterialMaster(OutWorkMaterialMaster);

		} else {
			System.out.println("Invalide Credantials");
		}
		return path;
	}

	public OutWorkMaterialMaster findOutWorkMaterialMasterById(int OutWorkMaterialMasterID) {
		return OutWorkMaterialMasterDao.findOutWorkMaterialMasterById(OutWorkMaterialMasterID);
	}

	public OutWorkMaterialMaster updateDeleteOutWorkMaterialMaster(OutWorkMaterialMaster OutWorkMaterialMaster)
			throws ClassNotFoundException, SQLException {

		return OutWorkMaterialMasterDao.updateDeleteOutWorkMaterialMaster(OutWorkMaterialMaster);
	}

	public String deleteOutWorkMaterialMasterById(int OutWorkMaterialMasterID) {
		OutWorkMaterialMasterDao.deleteOutWorkMaterialMasterById(OutWorkMaterialMasterID);
		return "deleted";
	}
}

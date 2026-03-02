package com.schoolapp.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.schoolapp.dao.OutWorkMaterialDao;

import com.schoolapp.dao.UserDao;
import com.schoolapp.entity.AccessPermission;
import com.schoolapp.entity.OutWorkMaterial;

import com.schoolapp.entity.User;

@Service
public class OutWorkMaterialService {

	@Autowired
	OutWorkMaterialDao OutWorkMaterialDao;

	int existInqMst = 0;

	public List<OutWorkMaterial> saveOutWorkMaterial(List<OutWorkMaterial> OutWorkMaterial)
			throws ClassNotFoundException, SQLException {

		List<OutWorkMaterial> path = null;
		int userId = 0;
		int flag = 0;

		for (OutWorkMaterial al : OutWorkMaterial) {
			if (flag == 0) {
				flag = 1;
				userId = al.getUserId();
			}
		}

		User users = new User();
		users.setuId(userId);

		AccessPermission accessPermission = new AccessPermission();
		accessPermission.setAccessUserId(userId);

		List<Object> al = new ArrayList<>();

		UserDao userDao = new UserDao();

		al.add(userDao.userValidation(users, accessPermission));

		int valideSave = accessPermission.getAccessCreate();
		if (valideSave == 1) {
			// path = workOrderRepo.save(workOrder);
			path = OutWorkMaterialDao.saveOutWork(OutWorkMaterial);

		} else {
			System.out.println("Invalide Credantials");
		}
		return path;

	}

	public String getAllOutWorkMaterial(OutWorkMaterial OutWorkMaterial) throws Exception {

		String path = null;
		int userId = OutWorkMaterial.getUserId();

		User users = new User();
		users.setuId(userId);

		AccessPermission accessPermission = new AccessPermission();
		accessPermission.setAccessUserId(userId);
		List<Object> al = new ArrayList<>();

		UserDao userDao = new UserDao();

		al.add(userDao.userValidation(users, accessPermission));

		int valideSave = accessPermission.getAccessCreate();
		if (valideSave == 1) {
			path = OutWorkMaterialDao.getAllOutWorkMaterial(OutWorkMaterial);

		} else {
			System.out.println("Invalide Credantials");
		}
		return path;
	}

	public String getAllStudentClassWise(OutWorkMaterial OutWorkMaterial) throws Exception {

		String path = null;
		int userId = OutWorkMaterial.getUserId();

		User users = new User();
		users.setuId(userId);

		AccessPermission accessPermission = new AccessPermission();
		accessPermission.setAccessUserId(userId);
		List<Object> al = new ArrayList<>();

		UserDao userDao = new UserDao();

		al.add(userDao.userValidation(users, accessPermission));

		int valideSave = accessPermission.getAccessCreate();
		if (valideSave == 1) {
			path = OutWorkMaterialDao.getAllOutWorkMaterial(OutWorkMaterial);

		} else {
			System.out.println("Invalide Credantials");
		}
		return path;
	}

	public OutWorkMaterial findOutWorkMaterialById(int OutWorkMaterialID) {
		return OutWorkMaterialDao.findOutWorkMaterialById(OutWorkMaterialID);
	}

	public OutWorkMaterial updateDeleteOutWorkMaterial(OutWorkMaterial OutWorkMaterial)
			throws ClassNotFoundException, SQLException {

		return OutWorkMaterialDao.updateDeleteOutWorkMaterial(OutWorkMaterial);
	}

	public String deleteOutWorkMaterialById(int OutWorkMaterialID) {
		OutWorkMaterialDao.deleteOutWorkMaterialById(OutWorkMaterialID);
		return "deleted";
	}

}

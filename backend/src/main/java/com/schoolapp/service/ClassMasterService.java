package com.schoolapp.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.schoolapp.dao.ClassMasterDao;
import com.schoolapp.dao.UserDao;
import com.schoolapp.entity.AccessPermission;
import com.schoolapp.entity.ClassMaster;
import com.schoolapp.entity.User;

@Service
public class ClassMasterService {
	@Autowired

	ClassMasterDao ClassMasterDao;

	public ClassMaster saveClassMaster(ClassMaster ClassMaster) {

		ClassMaster path = null;
		int userId = ClassMaster.getUserId();

		User users = new User();
		users.setuId(userId);

		AccessPermission accessPermission = new AccessPermission();
		accessPermission.setAccessUserId(userId);

		List<Object> al = new ArrayList<>();

		UserDao userDao = new UserDao();

		al.add(userDao.userValidation(users, accessPermission));

		int valideSave = accessPermission.getAccessCreate();
		if (valideSave == 1) {
			path = ClassMasterDao.saveClassMaster(ClassMaster);

		} else {
			System.out.println("Invalide Credantials");
		}
		return path;
	}

	public String getAllClassMaster(ClassMaster ClassMaster) {

		String path = null;
		int userId = ClassMaster.getUserId();

		System.out.println("user ID : " + userId);

		User users = new User();
		users.setuId(userId);

		AccessPermission accessPermission = new AccessPermission();
		accessPermission.setAccessUserId(userId);
		List<Object> al = new ArrayList<>();

		UserDao userDao = new UserDao();

		al.add(userDao.userValidation(users, accessPermission));

		int valideSave = accessPermission.getAccessCreate();

		if (valideSave == 1) {
			path = ClassMasterDao.getAllClassMaster(ClassMaster);

		} else {
			System.out.println("Invalide Credantials");
		}
		return path;
	}

	public ClassMaster findClassMasterById(int ClassMasterId) {
		return ClassMasterDao.findClassMasterById(ClassMasterId);
	}

	public ClassMaster updateClassMaster(ClassMaster ClassMasterId) {
		return ClassMasterDao.saveClassMaster(ClassMasterId);
	}

	public String deleteClassMasterById(int ClassMasterId) {
		ClassMasterDao.deleteClassMasterById(ClassMasterId);
		return "deleted";
	}
}

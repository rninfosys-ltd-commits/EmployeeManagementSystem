package com.schoolapp.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.schoolapp.dao.FeesStructureDao;
import com.schoolapp.dao.UserDao;
import com.schoolapp.entity.AccessPermission;
import com.schoolapp.entity.FeesStructure;
import com.schoolapp.entity.User;

@Service
public class FeesStructureService {
	@Autowired

	FeesStructureDao FeesStructureDao;

	public List<FeesStructure> saveFeesStructure(List<FeesStructure> FeesStructure)
			throws ClassNotFoundException, SQLException {

		List<FeesStructure> path = null;
		int userId = 0;
		int flag = 0;

		for (FeesStructure al : FeesStructure) {

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
			path = FeesStructureDao.saveFeesStructure(FeesStructure);

		} else {
			System.out.println("Invalide Credantials");
		}
		return path;
	}

	public String getAllFeesStructure(FeesStructure FeesStructure) throws ClassNotFoundException, SQLException {

		String path = null;

		int userId = FeesStructure.getUserId();
		User users = new User();
		users.setuId(userId);

		AccessPermission accessPermission = new AccessPermission();
		accessPermission.setAccessUserId(userId);

		List<Object> al = new ArrayList<>();

		UserDao userDao = new UserDao();

		al.add(userDao.userValidation(users, accessPermission));

		int valideSave = accessPermission.getAccessCreate();
		if (valideSave == 1) {
			path = FeesStructureDao.getAllFeesStructure(FeesStructure);

		} else {
			System.out.println("Invalide Credantials");
		}
		return path;
	}

	public FeesStructure findFeesStructureById(int FeesStructureId) {
		return FeesStructureDao.findFeesStructureById(FeesStructureId);
	}

	public List<FeesStructure> updateFeesStructure(List<FeesStructure> FeesStructureId)
			throws ClassNotFoundException, SQLException {

		return FeesStructureDao.saveFeesStructure((List<FeesStructure>) FeesStructureId);
	}

	public String deleteFeesStructureById(int FeesStructureId) {
		FeesStructureDao.deleteFeesStructureById(FeesStructureId);
		return "deleted";
	}
}

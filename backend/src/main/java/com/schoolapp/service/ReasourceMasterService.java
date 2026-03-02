package com.schoolapp.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.schoolapp.dao.ReasourceMasterDao;
import com.schoolapp.dao.UserDao;
import com.schoolapp.entity.AccessPermission;
import com.schoolapp.entity.ReasourceMaster;
import com.schoolapp.entity.User;

@Service
public class ReasourceMasterService {
	@Autowired

	ReasourceMasterDao ReasourceMasterDao;

	public ReasourceMaster saveReasourceMaster(ReasourceMaster ReasourceMaster)
			throws ClassNotFoundException, SQLException {

		ReasourceMaster path = null;
		int userId = ReasourceMaster.getUserId();

		User users = new User();
		users.setuId(userId);

		AccessPermission accessPermission = new AccessPermission();
		accessPermission.setAccessUserId(userId);

		List<Object> al = new ArrayList<>();

		UserDao userDao = new UserDao();

		al.add(userDao.userValidation(users, accessPermission));

		int valideSave = accessPermission.getAccessCreate();
		if (valideSave == 1) {
			path = ReasourceMasterDao.saveReasourceMaster(ReasourceMaster);

		} else {
			System.out.println("Invalide Credantials");
		}
		return path;
	}

	public String getAllReasourceMaster(ReasourceMaster ReasourceMaster) throws ClassNotFoundException, SQLException {
		String path = null;
		int userId = ReasourceMaster.getUserId();

		User users = new User();
		users.setuId(userId);

		AccessPermission accessPermission = new AccessPermission();
		accessPermission.setAccessUserId(userId);

		List<Object> al = new ArrayList<>();

		UserDao userDao = new UserDao();

		al.add(userDao.userValidation(users, accessPermission));

		int valideSave = accessPermission.getAccessCreate();
		if (valideSave == 1) {
			path = ReasourceMasterDao.getAllReasourceMaster(ReasourceMaster);

		} else {
			System.out.println("Invalide Credantials");
		}
		return path;
	}

	public ReasourceMaster findReasourceMasterById(int ReasourceMasterId) {
		return ReasourceMasterDao.findReasourceMasterById(ReasourceMasterId);
	}

	public ReasourceMaster updateReasourceMaster(ReasourceMaster ReasourceMasterId) {

		return ReasourceMasterDao.saveReasourceMaster(ReasourceMasterId);
	}

	public String deleteReasourceMasterById(int ReasourceMasterId) {
		ReasourceMasterDao.deleteReasourceMasterById(ReasourceMasterId);
		return "deleted";
	}
}

package com.schoolapp.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.schoolapp.dao.UserDao;
import com.schoolapp.dao.WingDao;
import com.schoolapp.entity.AccessPermission;
import com.schoolapp.entity.User;
import com.schoolapp.entity.Wing;

@Service
public class WingService {
	@Autowired
	WingDao wingDao;

	public Wing saveWing(Wing wing) {
		Wing path = null;
		int userId = wing.getUserId();

		User users = new User();
		users.setuId(userId);

		AccessPermission accessPermission = new AccessPermission();
		accessPermission.setAccessUserId(userId);

		List<Object> al = new ArrayList<>();

		UserDao userDao = new UserDao();

		al.add(userDao.userValidation(users, accessPermission));

		int valideSave = accessPermission.getAccessCreate();
		if (valideSave == 1) {
			path = wingDao.saveWing(wing);

		} else {
			System.out.println("Invalide Credantials");
		}
		return path;
	}

	public String updateWing(Wing wing) {
		String path = null;
		int userId = wing.getUserId();

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
			path = wingDao.updateWing(wing);

		} else {
			System.out.println("Invalide Credantials");
		}
		return path;
	}

	public Wing updateDeleteWing(Wing wing) {
		Wing path = null;
		int userId = wing.getUserId();

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
			path = wingDao.updateDeleteWing(wing);

		} else {
			System.out.println("Invalide Credantials");
		}
		return path;
	}

	public List<Wing> getAllWing() {
		return wingDao.getAllWing();
	}

	public Wing findWingById(int wing) {
		return wingDao.findWingById(wing);
	}

	public String deleteWingById(int wing) {
		wingDao.deleteWingById(wing);
		return "deleted";
	}

}

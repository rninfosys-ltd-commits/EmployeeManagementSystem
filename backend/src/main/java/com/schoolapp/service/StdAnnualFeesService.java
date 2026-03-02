package com.schoolapp.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.schoolapp.dao.StdAnnualFeesDao;
import com.schoolapp.dao.UserDao;
import com.schoolapp.entity.AccessPermission;
import com.schoolapp.entity.StdAnnualFees;
import com.schoolapp.entity.User;

@Service
public class StdAnnualFeesService {
	@Autowired

	StdAnnualFeesDao StdAnnualFeesDao;

	public StdAnnualFees saveStdAnnualFees(StdAnnualFees StdAnnualFees) {

		StdAnnualFees path = null;
		int userId = StdAnnualFees.getUserId();

		User users = new User();
		users.setuId(userId);

		AccessPermission accessPermission = new AccessPermission();
		accessPermission.setAccessUserId(userId);

		List<Object> al = new ArrayList<>();

		UserDao userDao = new UserDao();

		al.add(userDao.userValidation(users, accessPermission));

		int valideSave = accessPermission.getAccessCreate();
		if (valideSave == 1) {
			path = StdAnnualFeesDao.saveStdAnnualFees(StdAnnualFees);

		} else {
			System.out.println("Invalide Credantials");
		}
		return path;
	}

	public String getAllStdAnnualFees(StdAnnualFees StdAnnualFees) {

		String path = null;
		int userId = StdAnnualFees.getUserId();

		User users = new User();
		users.setuId(userId);

		AccessPermission accessPermission = new AccessPermission();
		accessPermission.setAccessUserId(userId);

		List<Object> al = new ArrayList<>();

		UserDao userDao = new UserDao();

		al.add(userDao.userValidation(users, accessPermission));

		int valideSave = accessPermission.getAccessCreate();
		if (valideSave == 1) {
			path = StdAnnualFeesDao.getAllStdAnnualFees(StdAnnualFees);

		} else {
			System.out.println("Invalide Credantials");
		}
		return path;
	}

	public String getYearWiseStdOutstanding(StdAnnualFees StdAnnualFees) {

		String path = null;
		int userId = StdAnnualFees.getUserId();

		User users = new User();
		users.setuId(userId);

		AccessPermission accessPermission = new AccessPermission();
		accessPermission.setAccessUserId(userId);

		List<Object> al = new ArrayList<>();

		UserDao userDao = new UserDao();

		al.add(userDao.userValidation(users, accessPermission));

		int valideSave = accessPermission.getAccessCreate();
		if (valideSave == 1) {
			path = StdAnnualFeesDao.getYearWiseStdOutstanding(StdAnnualFees);

		} else {
			System.out.println("Invalide Credantials");
		}
		return path;
	}

	public StdAnnualFees findStdAnnualFeesById(int StdAnnualFeesId) {
		return StdAnnualFeesDao.findStdAnnualFeesById(StdAnnualFeesId);
	}

	public StdAnnualFees updateStdAnnualFees(StdAnnualFees StdAnnualFeesId) {

		return StdAnnualFeesDao.saveStdAnnualFees(StdAnnualFeesId);
	}

	public String deleteStdAnnualFeesById(int StdAnnualFeesId) {
		StdAnnualFeesDao.deleteStdAnnualFeesById(StdAnnualFeesId);
		return "deleted";
	}
}

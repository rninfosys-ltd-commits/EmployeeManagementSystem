package com.schoolapp.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.schoolapp.dao.StdTransactionDetailesDao;
import com.schoolapp.dao.UserDao;
import com.schoolapp.entity.AccessPermission;
import com.schoolapp.entity.StdAnnualFees;
import com.schoolapp.entity.StdTransactionDetailes;
import com.schoolapp.entity.User;

@Service
public class StdTransactionDetailesService {
	@Autowired
	StdTransactionDetailesDao stdTransactionDetailesDao;

	public List<StdTransactionDetailes> saveStdTransactionDetailes(
			List<StdTransactionDetailes> stdTransactionDetailes) {

		List<StdTransactionDetailes> path = null;
		int userId = 0;
		int flag = 0;

		for (StdTransactionDetailes al : stdTransactionDetailes) {
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

			path = stdTransactionDetailesDao.saveStdTransactionDetailes(stdTransactionDetailes);

		} else {
			System.out.println("Invalide Credantials");
		}
		return path;

	}

	public String getPdfRcp(StdTransactionDetailes stdTransactionDetailes) {

		String path = null;
		int userId = stdTransactionDetailes.getUserId();

		User users = new User();
		users.setuId(userId);

		AccessPermission accessPermission = new AccessPermission();
		accessPermission.setAccessUserId(userId);

		List<Object> al = new ArrayList<>();

		UserDao userDao = new UserDao();

		al.add(userDao.userValidation(users, accessPermission));

		int valideSave = accessPermission.getAccessCreate();
		if (valideSave == 1) {
			path = stdTransactionDetailesDao.getPdfRcp(stdTransactionDetailes);

		} else {
			System.out.println("Invalide Credantials");
		}
		return path;
	}

	public String getAllStdTransactionDetailes(StdTransactionDetailes stdTransactionDetailes) {

		String path = null;
		int userId = stdTransactionDetailes.getUserId();

		User users = new User();
		users.setuId(userId);

		AccessPermission accessPermission = new AccessPermission();
		accessPermission.setAccessUserId(userId);

		List<Object> al = new ArrayList<>();

		UserDao userDao = new UserDao();

		al.add(userDao.userValidation(users, accessPermission));

		int valideSave = accessPermission.getAccessCreate();
		if (valideSave == 1) {
			path = stdTransactionDetailesDao.getAllStdTransactionDetailes(stdTransactionDetailes);

		} else {
			System.out.println("Invalide Credantials");
		}
		return path;
	}

	public String getAllTranMst(StdTransactionDetailes stdTransactionDetailes) {

		String path = null;
		int userId = stdTransactionDetailes.getUserId();

		User users = new User();
		users.setuId(userId);

		AccessPermission accessPermission = new AccessPermission();
		accessPermission.setAccessUserId(userId);

		List<Object> al = new ArrayList<>();

		UserDao userDao = new UserDao();

		al.add(userDao.userValidation(users, accessPermission));

		int valideSave = accessPermission.getAccessCreate();
		if (valideSave == 1) {
			path = stdTransactionDetailesDao.getAllTranMst(stdTransactionDetailes);

		} else {
			System.out.println("Invalide Credantials");
		}
		return path;
	}

	public String stdYearwiseFeesTrnDetailes(StdAnnualFees StdAnnualFees) {

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
			path = stdTransactionDetailesDao.stdYearwiseFeesTrnDetailes(StdAnnualFees);

		} else {
			System.out.println("Invalide Credantials");
		}
		return path;
	}

	public StdTransactionDetailes findStdTransactionDetailesById(int stdTransactionDetailesId) {
		return stdTransactionDetailesDao.findStdTransactionDetailesById(stdTransactionDetailesId);
	}

	public List<StdTransactionDetailes> updateStdTransactionDetailes(StdTransactionDetailes stdTransactionDetailes) {

		List<StdTransactionDetailes> list = new ArrayList<>();
		list.add(stdTransactionDetailes);
		return stdTransactionDetailesDao.saveStdTransactionDetailes(list);
	}

	public String deleteStdTransactionDetailesById(int stdTransactionDetailesId) {
		stdTransactionDetailesDao.deleteStdTransactionDetailesById(stdTransactionDetailesId);
		return "deleted";
	}
}

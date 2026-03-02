package com.schoolapp.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.schoolapp.dao.UserDao;
import com.schoolapp.entity.AccessPermission;
import com.schoolapp.entity.RecieptPaymentTransaction;
import com.schoolapp.entity.User;

@Service
public class RecieptPaymentTransactionService {
	@Autowired
	com.schoolapp.dao.RecieptPaymentTransactionDao RecieptPaymentTransactionDao;

	public RecieptPaymentTransaction saveRecieptPaymentTransaction(RecieptPaymentTransaction RecieptPaymentTransaction)
			throws ClassNotFoundException, SQLException {

		RecieptPaymentTransaction path = null;

		int userId = RecieptPaymentTransaction.getUserId();
		User users = new User();
		users.setuId(userId);

		AccessPermission accessPermission = new AccessPermission();
		accessPermission.setAccessUserId(userId);

		List<Object> al = new ArrayList<>();

		UserDao userDao = new UserDao();

		al.add(userDao.userValidation(users, accessPermission));

		int valideSave = accessPermission.getAccessCreate();

		if (valideSave == 1) {

			path = RecieptPaymentTransactionDao.saveRecieptPaymentTransaction(RecieptPaymentTransaction);

		} else {
			System.out.println("Invalide Credantials");
		}
		return path;

	}

	public String getAllRecieptPaymentTransaction(RecieptPaymentTransaction RecieptPaymentTransaction)
			throws ClassNotFoundException, SQLException {

		String path = null;
		int userId = RecieptPaymentTransaction.getUserId();

		User users = new User();
		users.setuId(userId);

		AccessPermission accessPermission = new AccessPermission();
		accessPermission.setAccessUserId(userId);

		List<Object> al = new ArrayList<>();

		UserDao userDao = new UserDao();

		al.add(userDao.userValidation(users, accessPermission));

		int valideSave = accessPermission.getAccessCreate();
		if (valideSave == 1) {
			path = RecieptPaymentTransactionDao.getAllRecieptPaymentTransaction(RecieptPaymentTransaction);

		} else {
			System.out.println("Invalide Credantials");
		}
		return path;
	}

	public String getAcStsmt(RecieptPaymentTransaction RecieptPaymentTransaction)
			throws ClassNotFoundException, SQLException {

		String path = null;
		int userId = RecieptPaymentTransaction.getUserId();

		User users = new User();
		users.setuId(userId);

		AccessPermission accessPermission = new AccessPermission();
		accessPermission.setAccessUserId(userId);

		List<Object> al = new ArrayList<>();

		UserDao userDao = new UserDao();

		al.add(userDao.userValidation(users, accessPermission));

		int valideSave = accessPermission.getAccessCreate();
		if (valideSave == 1) {
			path = RecieptPaymentTransactionDao.getAcStsmt(RecieptPaymentTransaction);

		} else {
			System.out.println("Invalide Credantials");
		}
		return path;
	}

}

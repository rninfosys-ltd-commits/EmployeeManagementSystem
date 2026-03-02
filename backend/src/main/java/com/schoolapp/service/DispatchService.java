package com.schoolapp.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.schoolapp.dao.DispatchDao;

import com.schoolapp.dao.UserDao;
import com.schoolapp.entity.AccessPermission;
import com.schoolapp.entity.Dispatch;

import com.schoolapp.entity.User;

@Service
public class DispatchService {

	@Autowired
	DispatchDao DispatchDao;

	int existInqMst = 0;

	public List<Dispatch> saveDispatch(List<Dispatch> Dispatch)
			throws ClassNotFoundException, SQLException {

		List<Dispatch> path = null;
		int userId = 0;
		int flag = 0;

		for (Dispatch al : Dispatch) {
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
			path = DispatchDao.saveDispatch(Dispatch);

		} else {
			System.out.println("Invalide Credantials");
		}
		return path;

	}

	public String getAllDispatch(Dispatch Dispatch) throws Exception {

		String path = null;
		int userId = Dispatch.getUserId();

		User users = new User();
		users.setuId(userId);

		AccessPermission accessPermission = new AccessPermission();
		accessPermission.setAccessUserId(userId);
		List<Object> al = new ArrayList<>();

		UserDao userDao = new UserDao();

		al.add(userDao.userValidation(users, accessPermission));

		int valideSave = accessPermission.getAccessCreate();
		if (valideSave == 1) {
			path = DispatchDao.getAllDispatch(Dispatch);

		} else {
			System.out.println("Invalide Credantials");
		}
		return path;
	}

	public String getAllStudentClassWise(Dispatch Dispatch) throws Exception {

		String path = null;
		int userId = Dispatch.getUserId();

		User users = new User();
		users.setuId(userId);

		AccessPermission accessPermission = new AccessPermission();
		accessPermission.setAccessUserId(userId);
		List<Object> al = new ArrayList<>();

		UserDao userDao = new UserDao();

		al.add(userDao.userValidation(users, accessPermission));

		int valideSave = accessPermission.getAccessCreate();
		if (valideSave == 1) {
			path = DispatchDao.getAllDispatch(Dispatch);

		} else {
			System.out.println("Invalide Credantials");
		}
		return path;
	}

	public Dispatch findDispatchById(int DispatchID) {
		return DispatchDao.findDispatchById(DispatchID);
	}

	public Dispatch updateDeleteDispatch(Dispatch Dispatch) throws ClassNotFoundException, SQLException {

		return DispatchDao.updateDeleteDispatch(Dispatch);
	}

	public String deleteDispatchById(int DispatchID) {
		DispatchDao.deleteDispatchById(DispatchID);
		return "deleted";
	}

}

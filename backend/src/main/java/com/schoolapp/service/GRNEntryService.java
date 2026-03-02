package com.schoolapp.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.schoolapp.dao.GRNEntryDao;
import com.schoolapp.dao.UserDao;
import com.schoolapp.entity.AccessPermission;
import com.schoolapp.entity.GRNEntry;
import com.schoolapp.entity.User;

@Service
public class GRNEntryService {

	@Autowired
	GRNEntryDao GRNEntryDao;

	int existInqMst = 0;

	public List<GRNEntry> saveGRNEntry(List<GRNEntry> GRNEntry) {

		List<GRNEntry> path = null;
		int userId = 0;
		int flag = 0;

		for (GRNEntry al : GRNEntry) {
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
			path = GRNEntryDao.saveGRNEntry(GRNEntry);

		} else {
			System.out.println("Invalide Credantials");
		}
		return path;

	}

	public String getAllGRNEntry(GRNEntry GRNEntry) {

		String path = null;
		int userId = GRNEntry.getUserId();

		User users = new User();
		users.setuId(userId);

		AccessPermission accessPermission = new AccessPermission();
		accessPermission.setAccessUserId(userId);
		List<Object> al = new ArrayList<>();

		UserDao userDao = new UserDao();

		al.add(userDao.userValidation(users, accessPermission));

		int valideSave = accessPermission.getAccessCreate();
		if (valideSave == 1) {
			path = GRNEntryDao.getAllGRNEntry(GRNEntry);

		} else {
			System.out.println("Invalide Credantials");
		}
		return path;
	}

	public String getAllStudentClassWise(GRNEntry GRNEntry) {

		String path = null;
		int userId = GRNEntry.getUserId();

		User users = new User();
		users.setuId(userId);

		AccessPermission accessPermission = new AccessPermission();
		accessPermission.setAccessUserId(userId);
		List<Object> al = new ArrayList<>();

		UserDao userDao = new UserDao();

		al.add(userDao.userValidation(users, accessPermission));

		int valideSave = accessPermission.getAccessCreate();
		if (valideSave == 1) {
			path = GRNEntryDao.getAllGRNEntry(GRNEntry);

		} else {
			System.out.println("Invalide Credantials");
		}
		return path;
	}

	public GRNEntry findGRNEntryById(int GRNEntryID) {
		return GRNEntryDao.findGRNEntryById(GRNEntryID);
	}

	public GRNEntry updateDeleteGRNEntry(GRNEntry GRNEntry) {

		return GRNEntryDao.updateDeleteGRNEntry(GRNEntry);
	}

	public String deleteGRNEntryById(int GRNEntryID) {
		GRNEntryDao.deleteGRNEntryById(GRNEntryID);
		return "deleted";
	}

}

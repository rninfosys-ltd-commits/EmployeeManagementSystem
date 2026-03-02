package com.schoolapp.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.schoolapp.dao.UnitDao;
import com.schoolapp.dao.UserDao;
import com.schoolapp.entity.Unit;
import com.schoolapp.entity.User;
import com.schoolapp.entity.AccessPermission;

@Service
public class UnitService {
	@Autowired
	UnitDao unitDao;

	public Unit saveUnit(Unit unit) {
		Unit path = null;
		int userId = unit.getUserId();

		User users = new User();
		users.setuId(userId);

		AccessPermission accessPermission = new AccessPermission();
		accessPermission.setAccessUserId(userId);

		List<Object> al = new ArrayList<>();

		UserDao userDao = new UserDao();

		al.add(userDao.userValidation(users, accessPermission));

		int valideSave = accessPermission.getAccessCreate();

		if (valideSave == 1) {
			path = unitDao.saveUnit(unit);

		} else {
			System.out.println("Invalide Credantials");
		}
		return path;

	}

	public List<Unit> getAllUnit() {
		return unitDao.getAllUnit();
	}

	public String getUnitSite(Unit unit) throws Exception {
		return unitDao.getUnitSite(unit);
	}

	public String getUnitWing(Unit unit) throws Exception {
		return unitDao.getUnitWing(unit);
	}

	public String getUnitFloor(Unit unit) throws Exception {
		return unitDao.getUnitFloor(unit);
	}

	public Unit findUnitById(int unit) {
		return unitDao.findUnitById(unit);
	}

	public String updateUnit(Unit unit) {
		String path = null;
		int userId = unit.getUserId();

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
			path = unitDao.updateUnit(unit);

		} else {
			System.out.println("Invalide Credantials");
		}
		return path;
	}

	public Unit updateDeleteUnit(Unit unit) {
		Unit path = null;
		int userId = unit.getUserId();

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
			path = unitDao.updateDeleteUnit(unit);

		} else {
			System.out.println("Invalide Credantials");
		}
		return path;
	}

	public String deleteUnitByID(int Unit) {
		unitDao.deleteUnitByID(Unit);
		return "Deleted..!";
	}
}

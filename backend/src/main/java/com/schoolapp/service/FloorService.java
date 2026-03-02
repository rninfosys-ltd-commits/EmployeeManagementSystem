package com.schoolapp.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.schoolapp.dao.FloorDao;
import com.schoolapp.dao.UserDao;
import com.schoolapp.entity.AccessPermission;
import com.schoolapp.entity.Floor;
import com.schoolapp.entity.User;

@Service
public class FloorService {
	@Autowired
	FloorDao floorDao;

	public Floor saveFloor(Floor floor) throws ClassNotFoundException, SQLException {

		Floor path = null;
		int userId = floor.getUserId();

		User users = new User();
		users.setuId(userId);

		AccessPermission accessPermission = new AccessPermission();
		accessPermission.setAccessUserId(userId);

		List<Object> al = new ArrayList<>();

		UserDao userDao = new UserDao();

		al.add(userDao.userValidation(users, accessPermission));

		int valideSave = accessPermission.getAccessCreate();

		if (valideSave == 1) {
			path = floorDao.saveFloor(floor);

		} else {
			System.out.println("Invalide Credantials");
		}
		return path;
	}

	public String getAllFloor() throws ClassNotFoundException, SQLException {
		return floorDao.getAllFloor();
	}

	public String getFloorSite(Floor floor) throws Exception {
		return floorDao.getFloorSite(floor);
	}

	public String getFloorWing(Floor floor) throws Exception {
		return floorDao.getFloorWing(floor);
	}

	public String updateFloor(Floor floor) throws ClassNotFoundException, SQLException {
		String path = null;
		int userId = floor.getUserId();

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
			path = floorDao.updateFloor(floor);

		} else {
			System.out.println("Invalide Credantials");
		}
		return path;
	}

	public Floor updateDeleteFloor(Floor floor) throws ClassNotFoundException, SQLException {
		Floor path = null;
		int userId = floor.getUserId();

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
			path = floorDao.updateDeleteFloor(floor);

		} else {
			System.out.println("Invalide Credantials");
		}
		return path;
	}

	public Floor findFloorById(int floor) {

		return floorDao.findFloorById(floor);
	}

	public String deleteFloorById(int floor) {
		floorDao.deleteFloorByID(floor);
		return "deleted";
	}
}

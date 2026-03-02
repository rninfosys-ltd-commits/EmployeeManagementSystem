package com.schoolapp.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.schoolapp.dao.AttendanceDetailesDao;
import com.schoolapp.dao.UserDao;
import com.schoolapp.entity.AccessPermission;
import com.schoolapp.entity.AttendanceDetailes;
import com.schoolapp.entity.User;

@Service
public class AttendanceDetailesService {
	@Autowired
	AttendanceDetailesDao AttendanceDetailesDao;

	public List<AttendanceDetailes> saveAttendanceDetailes(ArrayList<AttendanceDetailes> AttendanceDetailes)
			throws ClassNotFoundException, SQLException {
		List<AttendanceDetailes> path = null;
		int userId = 0;
		int flag = 0;

		for (AttendanceDetailes al : AttendanceDetailes) {

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
			path = AttendanceDetailesDao.saveAttendanceDetailes(AttendanceDetailes);

		} else {
			System.out.println("Invalide Credantials");
		}
		return path;

	}

	public String getAllAttendanceDetailes(AttendanceDetailes AttendanceDetailes) throws Exception {

		String path = null;
		int userId = AttendanceDetailes.getUserId();

		User users = new User();
		users.setuId(userId);

		AccessPermission accessPermission = new AccessPermission();
		accessPermission.setAccessUserId(userId);

		List<Object> al = new ArrayList<>();

		UserDao userDao = new UserDao();

		al.add(userDao.userValidation(users, accessPermission));

		int valideSave = accessPermission.getAccessUpdate();
		if (valideSave == 1) {
			path = AttendanceDetailesDao.getAllAttendanceDetailes(AttendanceDetailes);

		} else {
			System.out.println("Invalide Credantials");
		}
		return path;
	}

	public AttendanceDetailes updateDeleteAttendanceDetailes(AttendanceDetailes AttendanceDetailes)
			throws ClassNotFoundException, SQLException {

		AttendanceDetailes path = null;
		int userId = AttendanceDetailes.getUserId();

		User users = new User();
		users.setuId(userId);

		AccessPermission accessPermission = new AccessPermission();
		accessPermission.setAccessUserId(userId);

		List<Object> al = new ArrayList<>();

		UserDao userDao = new UserDao();

		al.add(userDao.userValidation(users, accessPermission));

		int valideSave = accessPermission.getAccessUpdate();
		if (valideSave == 1) {
			path = AttendanceDetailesDao.updateDeleteAttendanceDetailes(AttendanceDetailes);

		} else {
			System.out.println("Invalide Credantials");
		}
		return path;

	}

	public AttendanceDetailes findAttendanceDetailesById(int AttendanceDetailesId) {
		return AttendanceDetailesDao.findAttendanceDetailesById(AttendanceDetailesId);
	}

	public ArrayList<AttendanceDetailes> updateAttendanceDetailes(ArrayList<AttendanceDetailes> AttendanceDetailes)
			throws ClassNotFoundException, SQLException {

		return AttendanceDetailesDao.saveAttendanceDetailes(AttendanceDetailes);
	}

	public String deleteAttendanceDetailesById(int AttendanceDetailesId) {
		AttendanceDetailesDao.deleteAttendanceDetailesById(AttendanceDetailesId);
		return "deleted";
	}
}

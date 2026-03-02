package com.schoolapp.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.schoolapp.dao.StudentInfoDao;
import com.schoolapp.dao.UserDao;
import com.schoolapp.entity.AccessPermission;
import com.schoolapp.entity.StudentInfo;
import com.schoolapp.entity.User;

@Service
public class StudentInfoService {
	@Autowired
	StudentInfoDao studentInfoDao;

	public StudentInfo saveStudentInfo(StudentInfo studentInfo) {

		StudentInfo path = null;
		int userId = studentInfo.getUserId();

		User users = new User();
		users.setuId(userId);

		AccessPermission accessPermission = new AccessPermission();
		accessPermission.setAccessUserId(userId);

		List<Object> al = new ArrayList<>();

		UserDao userDao = new UserDao();

		al.add(userDao.userValidation(users, accessPermission));

		int valideSave = accessPermission.getAccessCreate();
		if (valideSave == 1) {
			path = studentInfoDao.saveStudentInfo(studentInfo);

		} else {
			System.out.println("Invalide Credantials");
		}
		return path;
	}

	public String getAllStudentInfo(StudentInfo studentInfo) {
		String path = null;
		int userId = studentInfo.getUserId();

		User users = new User();
		users.setuId(userId);

		AccessPermission accessPermission = new AccessPermission();
		accessPermission.setAccessUserId(userId);

		List<Object> al = new ArrayList<>();

		UserDao userDao = new UserDao();

		al.add(userDao.userValidation(users, accessPermission));

		int valideSave = accessPermission.getAccessCreate();
		if (valideSave == 1) {
			path = studentInfoDao.getAllStudentInfo(studentInfo);

		} else {
			System.out.println("Invalide Credantials");
		}
		return path;
	}

	public StudentInfo findStudentInfoById(int StudentInfoID) {
		return studentInfoDao.findStudentInfoById(StudentInfoID);
	}

	public String updateStudentInfo(StudentInfo studentInfo) {

		return studentInfoDao.updateStudentInfo(studentInfo);
	}

	public String deleteStudentInfoById(int StudentInfoID) {
		studentInfoDao.deleteStudentInfoById(StudentInfoID);
		return "deleted";
	}
}

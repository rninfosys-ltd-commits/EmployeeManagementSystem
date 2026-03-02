package com.schoolapp.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.schoolapp.dao.AdmissionDao;
import com.schoolapp.dao.UserDao;
import com.schoolapp.entity.AccessPermission;
import com.schoolapp.entity.Admission;
import com.schoolapp.entity.User;

@Service
public class AdmissionService {

	@Autowired
	AdmissionDao admissionDao;

	public Object saveAdmission(Admission admission)
			throws ClassNotFoundException, SQLException {

		Object path = null;
		int userId = admission.getUserId();

		User users = new User();
		users.setuId(userId);

		AccessPermission accessPermission = new AccessPermission();
		accessPermission.setAccessUserId(userId);

		List<Object> al = new ArrayList<>();

		UserDao userDao = new UserDao();

		al.add(userDao.userValidation(users, accessPermission));

		int valideSave = accessPermission.getAccessCreate();
		if (valideSave == 1) {

			path = admissionDao.saveAdmission(admission);

		} else {
			System.out.println("Invalide Credantials");
		}
		return path;

	}

	public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file)
			throws ClassNotFoundException, SQLException {

		ResponseEntity<String> path = null;

		path = admissionDao.uploadImage(file);

		return path;

	}

	public String getAllAdmission(Admission admission) throws ClassNotFoundException, SQLException {

		String path = null;
		int userId = admission.getUserId();

		User users = new User();
		users.setuId(userId);

		AccessPermission accessPermission = new AccessPermission();
		accessPermission.setAccessUserId(userId);
		List<Object> al = new ArrayList<>();

		UserDao userDao = new UserDao();

		al.add(userDao.userValidation(users, accessPermission));

		int valideSave = accessPermission.getAccessCreate();
		if (valideSave == 1) {
			path = admissionDao.getAllAdmission(admission);

		} else {
			System.out.println("Invalide Credantials");
		}
		return path;
	}

	public String getAllStudentClassWise(Admission admission) throws ClassNotFoundException, SQLException {

		String path = null;
		int userId = admission.getUserId();

		User users = new User();
		users.setuId(userId);

		AccessPermission accessPermission = new AccessPermission();
		accessPermission.setAccessUserId(userId);
		List<Object> al = new ArrayList<>();

		UserDao userDao = new UserDao();

		al.add(userDao.userValidation(users, accessPermission));

		int valideSave = accessPermission.getAccessCreate();
		if (valideSave == 1) {
			path = admissionDao.getAllStudentClassWise(admission);

		} else {
			System.out.println("Invalide Credantials");
		}
		return path;
	}

	public String getPdfBonafied(Admission admission) throws ClassNotFoundException, SQLException {

		String path = null;
		int userId = admission.getUserId();

		User users = new User();
		users.setuId(userId);

		AccessPermission accessPermission = new AccessPermission();
		accessPermission.setAccessUserId(userId);
		List<Object> al = new ArrayList<>();

		UserDao userDao = new UserDao();

		al.add(userDao.userValidation(users, accessPermission));

		int valideSave = accessPermission.getAccessCreate();
		if (valideSave == 1) {
			path = admissionDao.getPdfBonafied(admission);

		} else {
			System.out.println("Invalide Credantials");
		}
		return path;
	}

	public Admission findAdmissionById(int admissionID) {
		return admissionDao.findAdmissionById(admissionID);
	}

	public String updateAdmission(Admission admission) throws ClassNotFoundException, SQLException {

		return admissionDao.updateAdmission(admission);
	}

	public String deleteAdmissionById(int admissionID) {
		admissionDao.deleteAdmissionById(admissionID);
		return "deleted";
	}
}

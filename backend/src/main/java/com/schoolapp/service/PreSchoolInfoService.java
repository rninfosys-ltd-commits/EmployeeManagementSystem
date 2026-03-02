package com.schoolapp.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.schoolapp.dao.PreSchoolInfoDao;
import com.schoolapp.dao.UserDao;
import com.schoolapp.entity.AccessPermission;
import com.schoolapp.entity.PreSchoolInfo;
import com.schoolapp.entity.User;

@Service
public class PreSchoolInfoService {
	@Autowired
	PreSchoolInfoDao preSchoolInfoDao;

	public PreSchoolInfo savePreSchoolInfo(PreSchoolInfo preSchoolInfo) {
		PreSchoolInfo path = null;
		int userId = preSchoolInfo.getUserId();

		User users = new User();
		users.setuId(userId);

		AccessPermission accessPermission = new AccessPermission();
		accessPermission.setAccessUserId(userId);

		List<Object> al = new ArrayList<>();

		UserDao userDao = new UserDao();

		al.add(userDao.userValidation(users, accessPermission));

		int valideSave = accessPermission.getAccessCreate();
		if (valideSave == 1) {
			path = preSchoolInfoDao.savePreSchoolInfo(preSchoolInfo);

		} else {
			System.out.println("Invalide Credantials");
		}
		return path;
	}

	public String getAllPreSchoolInfo(PreSchoolInfo preSchoolInfo) {
		String path = null;
		int userId = preSchoolInfo.getUserId();

		User users = new User();
		users.setuId(userId);

		AccessPermission accessPermission = new AccessPermission();
		accessPermission.setAccessUserId(userId);

		List<Object> al = new ArrayList<>();

		UserDao userDao = new UserDao();

		al.add(userDao.userValidation(users, accessPermission));

		int valideSave = accessPermission.getAccessCreate();
		if (valideSave == 1) {
			path = preSchoolInfoDao.getAllPreSchoolInfo(preSchoolInfo);

		} else {
			System.out.println("Invalide Credantials");
		}
		return path;
	}

	public PreSchoolInfo findPreSchoolInfoById(int preSchoolInfoID) {
		return preSchoolInfoDao.findPreSchoolInfoById(preSchoolInfoID);
	}

	public String updatePreSchoolInfo(PreSchoolInfo preSchoolInfo) {

		return preSchoolInfoDao.updatePreSchoolInfo(preSchoolInfo);
	}

	public String deletePreSchoolInfoById(int preSchoolInfoId) {
		preSchoolInfoDao.deletePreSchoolInfoById(preSchoolInfoId);
		return "deleted";
	}
}

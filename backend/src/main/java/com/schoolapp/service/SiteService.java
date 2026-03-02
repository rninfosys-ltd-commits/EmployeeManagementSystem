package com.schoolapp.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.schoolapp.dao.SiteDao;
import com.schoolapp.dao.UserDao;
import com.schoolapp.entity.AccessPermission;
import com.schoolapp.entity.Site;
import com.schoolapp.entity.User;

@Service
public class SiteService {
	@Autowired
	SiteDao siteDao;

	public Site saveSite(Site site) {
		Site path = null;
		int userId = site.getUserId();

		User users = new User();
		users.setuId(userId);

		AccessPermission accessPermission = new AccessPermission();
		accessPermission.setAccessUserId(userId);

		List<Object> al = new ArrayList<>();

		UserDao userDao = new UserDao();

		al.add(userDao.userValidation(users, accessPermission));

		int valideSave = accessPermission.getAccessCreate();

		if (valideSave == 1) {
			path = siteDao.saveSite(site);

		} else {
			System.out.println("Invalide Credantials");
		}
		return path;
	}

	public List<Site> getAllSite() {
		return siteDao.getAllSite();
	}

	public Site findStateById(int siteId) {
		return siteDao.findSiteById(siteId);
	}

	public String updateSite(Site site) {
		String path = null;
		int userId = site.getUserId();

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
			path = siteDao.updateSite(site);

		} else {
			System.out.println("Invalide Credantials");
		}
		return path;
	}

	public Site updateDeleteSite(Site site) {
		Site path = null;
		int userId = site.getUserId();

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
			path = siteDao.updateDeleteSite(site);

		} else {
			System.out.println("Invalide Credantials");
		}
		return path;
	}

	public String deleteSiteById(int siteId) {
		siteDao.deleteSiteById(siteId);
		return "deleted";
	}
}

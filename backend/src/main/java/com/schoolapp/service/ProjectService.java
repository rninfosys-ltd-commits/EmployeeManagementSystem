package com.schoolapp.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.schoolapp.dao.ProjectDao;
import com.schoolapp.dao.UserDao;
import com.schoolapp.entity.AccessPermission;
import com.schoolapp.entity.Project;
import com.schoolapp.entity.User;

@Service
public class ProjectService {

	@Autowired

	ProjectDao projectDao;

	public Project saveProject(Project project) throws ClassNotFoundException, SQLException {
		Project path = null;
		int userId = project.getUserId();

		User users = new User();
		users.setuId(userId);

		AccessPermission accessPermission = new AccessPermission();
		accessPermission.setAccessUserId(userId);

		List<Object> al = new ArrayList<>();

		UserDao userDao = new UserDao();

		al.add(userDao.userValidation(users, accessPermission));

		int valideSave = accessPermission.getAccessCreate();

		if (valideSave == 1) {
			path = projectDao.saveProject(project);

		} else {
			System.out.println("Invalide Credantials");
		}
		return path;

	}

	public List<Project> getAllProject() {
		return projectDao.getAllProject();
	}

	public Project findProjectById(int project) {
		return projectDao.findProjectById(project);
	}

	public Project updateDeleteProject(Project project) throws ClassNotFoundException, SQLException {
		Project path = null;
		int userId = project.getUserId();

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
			path = projectDao.updateDeleteProject(project);

		} else {
			System.out.println("Invalide Credantials");
		}
		return path;
	}

	public String updateProject(Project project) throws ClassNotFoundException, SQLException {
		String path = null;
		int userId = project.getUserId();

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
			path = projectDao.updateProject(project);

		} else {
			System.out.println("Invalide Credantials");
		}
		return path;
	}

	public String deleteProjectById(int project) {
		projectDao.deleteUserById(project);
		return " deleted ";
	}

}

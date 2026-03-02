package com.schoolapp.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.schoolapp.dao.UserDao;
import com.schoolapp.entity.AccessPermission;
import com.schoolapp.entity.User;
import com.schoolapp.entity.UserEntity;
import com.schoolapp.repository.UserRepository;

@Service
public class UserService {

	private final UserDao userDao;
	private final UserRepository userRepository;

	@Autowired
	public UserService(UserDao userDao, UserRepository userRepository) {
		this.userDao = userDao;
		this.userRepository = userRepository;
	}

	public User saveUser(User user) throws ClassNotFoundException, SQLException {
		User path = null;
		path = userDao.saveUser(user);
		return path;
	}

	public String getAllUser(User user) throws ClassNotFoundException, SQLException {

		String path = null;
		int userId = user.getUserId();

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
			path = userDao.getAllUser(user);

		} else {
			System.out.println("Invalide Credantials");
		}
		return path;

	}

	public String valideUserDetailes(User users) throws ClassNotFoundException, SQLException {
		return userDao.valideUserDetailes(users);
	}

	public AccessPermission userValidation(User users, AccessPermission accessPermission)
			throws ClassNotFoundException, SQLException {
		return userDao.userValidation(users, accessPermission);
	}

	public User findUserById(int UserId) {
		return userDao.findUserById(UserId);
	}

	public String updateUser(User user) throws ClassNotFoundException, SQLException {
		String path = null;
		int userId = user.getUserId();

		User users = new User();
		users.setuId(userId);

		AccessPermission accessPermission = new AccessPermission();
		accessPermission.setAccessUserId(userId);

		List<Object> al = new ArrayList<>();

		UserDao userDao = new UserDao();

		al.add(userDao.userValidation(users, accessPermission));

		int valideSave = accessPermission.getAccessUpdate();

		if (valideSave == 1) {
			path = userDao.updateUser(user);

		} else {
			System.out.println("Invalide Credantials");
		}
		return path;
	}

	public String updateDeleteUser(User users) throws ClassNotFoundException, SQLException {
		String path = null;
		int userId = users.getUserId();

		User user = new User();
		user.setuId(userId);

		AccessPermission accessPermission = new AccessPermission();
		accessPermission.setAccessUserId(userId);

		List<Object> al = new ArrayList<>();

		UserDao userDao = new UserDao();

		al.add(userDao.userValidation(users, accessPermission));
		int valideSave = accessPermission.getAccessUpdate();

		if (valideSave == 1) {
			path = userDao.updateDeleteUser(users);

		} else {
			System.out.println("Invalide Credantials");
		}
		return path;
	}

	public User updateUsers(User User) throws ClassNotFoundException, SQLException {
		User us = userDao.findUserById(User.getuId());
		us.setuId(User.getuId());
		us.setUserSrNo(User.getUserSrNo());
		us.setUserName(User.getUserName());
		us.setBranchId(User.getBranchId());
		us.setOrgId(User.getOrgId());
		us.setDate(User.getDate());
		us.setUserContact(User.getUserContact());
		us.setPanNo(User.getPanNo());
		us.setGstNo(User.getGstNo());
		us.setEmail(User.getEmail());
		us.setPhone(User.getPhone());
		us.setuAddress(User.getuAddress());
		us.setIncome(User.getIncome());
		us.setIncomeSource(User.getIncomeSource());
		us.setOtherIncome(User.getOtherIncome());
		us.setOtherIncomeSource(User.getOtherIncomeSource());
		us.setNotes(User.getNotes());
		us.setIsActive(User.getIsActive());
		us.setStateId(User.getStateId());
		us.setDistId(User.getDistId());
		us.setCityId(User.getCityId());
		us.setUserId(User.getUserId());
		us.setCreatedDate(User.getCreatedDate());
		us.setUpdatedBy(User.getUpdatedBy());
		us.setUpdatedDate(User.getUpdatedDate());
		return userDao.saveUser(us);
	}

	public String deleteUserById(int userId) {
		userDao.deleteUserById(userId);
		return "deleted";
	}

	public List<UserEntity> getParties() {
		return userRepository.findByRole("ROLE_PARTY_NAME");
	}

}

package com.schoolapp.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.schoolapp.dao.ContractorDao;
import com.schoolapp.dao.UserDao;
import com.schoolapp.entity.AccessPermission;
import com.schoolapp.entity.Contractor;
import com.schoolapp.entity.User;

@Service
public class ContractorService {
	@Autowired
	ContractorDao contractorDao;

	public Contractor saveContractor(Contractor contractor) {

		Contractor path = null;
		int userId = contractor.getUserId();

		User users = new User();
		users.setuId(userId);

		AccessPermission accessPermission = new AccessPermission();
		accessPermission.setAccessUserId(userId);

		List<Object> al = new ArrayList<>();

		UserDao userDao = new UserDao();

		al.add(userDao.userValidation(users, accessPermission));

		int valideSave = accessPermission.getAccessCreate();

		if (valideSave == 1) {
			path = contractorDao.saveContractor(contractor);
		} else {
			System.out.println("Invalide Credantials");
		}
		return path;
	}

	public List<Contractor> getAllContractor() {
		return contractorDao.getAllContractor();
	}

	public Contractor findContractorById(int ContractorId) {
		return contractorDao.findContractorById(ContractorId);
	}

	public String updateContractor(Contractor contractor) {

		String path = null;
		int userId = contractor.getUserId();

		User users = new User();
		users.setuId(userId);

		AccessPermission accessPermission = new AccessPermission();
		accessPermission.setAccessUserId(userId);

		List<Object> al = new ArrayList<>();

		UserDao userDao = new UserDao();

		al.add(userDao.userValidation(users, accessPermission));

		int valideSave = accessPermission.getAccessUpdate();

		// System.out.println("im here : " + valideSave);

		if (valideSave == 1) {
			path = contractorDao.updateContractor(contractor);

		} else {
			System.out.println("Invalide Credantials");
		}
		return path;

	}

	public Contractor updateDeleteContractor(Contractor contractor) {
		Contractor path = null;
		int userId = contractor.getUserId();

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
			path = contractorDao.updateDeleteContractor(contractor);

		} else {
			System.out.println("Invalide Credantials");
		}
		return path;
	}

	public String deleteContractorById(int ContractorId) {
		contractorDao.deleteContractorById(ContractorId);
		return "deleted";
	}
}

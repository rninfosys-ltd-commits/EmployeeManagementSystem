package com.schoolapp.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.schoolapp.dao.ProductionRecipeMasterDao;
import com.schoolapp.dao.UserDao;
import com.schoolapp.entity.AccessPermission;
import com.schoolapp.entity.ProductionRecipeMaster;
import com.schoolapp.entity.User;

@Service
public class ProductionRecipeMasterService {
	@Autowired
	ProductionRecipeMasterDao ProductionRecipeMasterDao;

	public Object saveProductionRecipeMaster(ProductionRecipeMaster ProductionRecipeMaster)
			throws ClassNotFoundException, SQLException {

		Object path = null;
		int userId = ProductionRecipeMaster.getUserId();

		User users = new User();
		users.setuId(userId);

		AccessPermission accessPermission = new AccessPermission();
		accessPermission.setAccessUserId(userId);

		List<Object> al = new ArrayList<>();

		UserDao userDao = new UserDao();

		al.add(userDao.userValidation(users, accessPermission));

		int valideSave = accessPermission.getAccessCreate();
		if (valideSave == 1) {

			path = ProductionRecipeMasterDao.saveProductionRecipeMaster(ProductionRecipeMaster);

		} else {
			System.out.println("Invalide Credantials");
		}
		return path;

	}

	public String getAllProductionRecipeMaster(ProductionRecipeMaster ProductionRecipeMaster) throws Exception {

		String path = null;
		int userId = ProductionRecipeMaster.getUserId();

		User users = new User();
		users.setuId(userId);

		AccessPermission accessPermission = new AccessPermission();
		accessPermission.setAccessUserId(userId);
		List<Object> al = new ArrayList<>();

		UserDao userDao = new UserDao();

		al.add(userDao.userValidation(users, accessPermission));

		int valideSave = accessPermission.getAccessCreate();
		if (valideSave == 1) {
			path = ProductionRecipeMasterDao.getAllProductionRecipeMaster(ProductionRecipeMaster);

		} else {
			System.out.println("Invalide Credantials");
		}
		return path;
	}

	public String getAllStudentClassWise(ProductionRecipeMaster ProductionRecipeMaster) throws Exception {

		String path = null;
		int userId = ProductionRecipeMaster.getUserId();

		User users = new User();
		users.setuId(userId);

		AccessPermission accessPermission = new AccessPermission();
		accessPermission.setAccessUserId(userId);
		List<Object> al = new ArrayList<>();

		UserDao userDao = new UserDao();

		al.add(userDao.userValidation(users, accessPermission));

		int valideSave = accessPermission.getAccessCreate();
		if (valideSave == 1) {
			path = ProductionRecipeMasterDao.getAllProductionRecipeMaster(ProductionRecipeMaster);

		} else {
			System.out.println("Invalide Credantials");
		}
		return path;
	}

	public ProductionRecipeMaster findProductionRecipeMasterById(int ProductionRecipeMasterID) {
		return ProductionRecipeMasterDao.findProductionRecipeMasterById(ProductionRecipeMasterID);
	}

	public ProductionRecipeMaster updateDeleteProductionRecipeMaster(ProductionRecipeMaster ProductionRecipeMaster)
			throws ClassNotFoundException, SQLException {

		return ProductionRecipeMasterDao.updateDeleteProductionRecipeMaster(ProductionRecipeMaster);
	}

	public String deleteProductionRecipeMasterById(int ProductionRecipeMasterID) {
		ProductionRecipeMasterDao.deleteProductionRecipeMasterById(ProductionRecipeMasterID);
		return "deleted";
	}

}

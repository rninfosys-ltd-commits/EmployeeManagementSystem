package com.schoolapp.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.schoolapp.dao.ProductionRecipeDao;
import com.schoolapp.dao.UserDao;
import com.schoolapp.entity.AccessPermission;
import com.schoolapp.entity.ProductionRecipe;
import com.schoolapp.entity.User;

@Service
public class ProductionRecipeService {

	@Autowired
	ProductionRecipeDao ProductionRecipeDao;

	public Object saveProductionRecipe(ProductionRecipe ProductionRecipe) {

		Object path = null;
		int userId = ProductionRecipe.getUserId();

		User users = new User();
		users.setuId(userId);

		AccessPermission accessPermission = new AccessPermission();
		accessPermission.setAccessUserId(userId);

		List<Object> al = new ArrayList<>();

		UserDao userDao = new UserDao();

		al.add(userDao.userValidation(users, accessPermission));

		int valideSave = accessPermission.getAccessCreate();
		if (valideSave == 1) {

			path = ProductionRecipeDao.saveProductionRecipe(ProductionRecipe);

		} else {
			System.out.println("Invalide Credantials");
		}
		return path;

	}

	public String getAllProductionRecipe(ProductionRecipe ProductionRecipe) {

		String path = null;
		int userId = ProductionRecipe.getUserId();

		User users = new User();
		users.setuId(userId);

		AccessPermission accessPermission = new AccessPermission();
		accessPermission.setAccessUserId(userId);
		List<Object> al = new ArrayList<>();

		UserDao userDao = new UserDao();

		al.add(userDao.userValidation(users, accessPermission));

		int valideSave = accessPermission.getAccessCreate();
		if (valideSave == 1) {
			path = ProductionRecipeDao.getAllProductionRecipe(ProductionRecipe);

		} else {
			System.out.println("Invalide Credantials");
		}
		return path;
	}

	public String getAllStudentClassWise(ProductionRecipe ProductionRecipe) {

		String path = null;
		int userId = ProductionRecipe.getUserId();

		User users = new User();
		users.setuId(userId);

		AccessPermission accessPermission = new AccessPermission();
		accessPermission.setAccessUserId(userId);
		List<Object> al = new ArrayList<>();

		UserDao userDao = new UserDao();

		al.add(userDao.userValidation(users, accessPermission));

		int valideSave = accessPermission.getAccessCreate();
		if (valideSave == 1) {
			path = ProductionRecipeDao.getAllProductionRecipe(ProductionRecipe);

		} else {
			System.out.println("Invalide Credantials");
		}
		return path;
	}

	public ProductionRecipe findProductionRecipeById(int ProductionRecipeID) {
		return ProductionRecipeDao.findProductionRecipeById(ProductionRecipeID);
	}

	public ProductionRecipe updateDeleteProductionRecipe(ProductionRecipe ProductionRecipe) {

		return ProductionRecipeDao.updateDeleteProductionRecipe(ProductionRecipe);
	}

	public String deleteProductionRecipeById(int ProductionRecipeID) {
		ProductionRecipeDao.deleteProductionRecipeById(ProductionRecipeID);
		return "deleted";
	}

}

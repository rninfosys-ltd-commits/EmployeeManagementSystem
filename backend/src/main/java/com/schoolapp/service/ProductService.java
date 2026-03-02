package com.schoolapp.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.schoolapp.dao.ProductDao;
import com.schoolapp.dao.UserDao;
import com.schoolapp.entity.AccessPermission;
import com.schoolapp.entity.Product;
import com.schoolapp.entity.User;

@Service
public class ProductService {
	@Autowired
	ProductDao productDao;

	public Product saveProduct(Product product) throws ClassNotFoundException, SQLException {
		Product path = null;
		int userId = product.getUserId();

		User users = new User();
		users.setuId(userId);

		AccessPermission accessPermission = new AccessPermission();
		accessPermission.setAccessUserId(userId);

		List<Object> al = new ArrayList<>();

		UserDao userDao = new UserDao();

		al.add(userDao.userValidation(users, accessPermission));

		int valideSave = accessPermission.getAccessCreate();
		if (valideSave == 1) {
			path = productDao.saveProduct(product);

		} else {
			System.out.println("Invalide Credantials");
		}
		return path;

	}

	public List<Product> getAllProduct() {
		return productDao.getAllProduct();
	}

	public Product findProductById(int Product) {
		return productDao.findProductById(Product);
	}

	public Product updateDeleteProduct(Product product) throws ClassNotFoundException, SQLException {
		Product path = null;
		int userId = product.getUserId();

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
			path = productDao.updateDeleteProduct(product);

		} else {
			System.out.println("Invalide Credantials");
		}
		return path;
	}

	public String updateProduct(Product product) throws ClassNotFoundException, SQLException {
		String path = null;
		int userId = product.getUserId();

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
			path = productDao.updateProduct(product);

		} else {
			System.out.println("Invalide Credantials");
		}
		return path;
	}

	public String deleteProductById(int product) {
		productDao.deleteProductById(product);
		return "deleted";
	}
}

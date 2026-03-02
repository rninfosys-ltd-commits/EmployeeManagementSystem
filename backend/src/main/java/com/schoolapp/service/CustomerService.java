package com.schoolapp.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.schoolapp.dao.CustomerDao;
import com.schoolapp.dao.UserDao;
import com.schoolapp.entity.AccessPermission;
import com.schoolapp.entity.Customer;
import com.schoolapp.entity.User;

@Service
public class CustomerService {

	@Autowired
	CustomerDao customerDao;

	public Customer saveCustomer(Customer customer) throws ClassNotFoundException, SQLException {
		Customer path = null;
		int userId = customer.getUserId();

		User users = new User();
		users.setuId(userId);

		AccessPermission accessPermission = new AccessPermission();
		accessPermission.setAccessUserId(userId);

		List<Object> al = new ArrayList<>();

		UserDao userDao = new UserDao();

		al.add(userDao.userValidation(users, accessPermission));

		int valideSave = accessPermission.getAccessCreate();
		if (valideSave == 1) {
			path = customerDao.saveCustomer(customer);

		} else {
			System.out.println("Invalide Credantials");
		}
		return path;
	}

	public String getAllCustomer(Customer customer) throws ClassNotFoundException, SQLException {

		String path = null;
		int userId = customer.getUserId();

		User users = new User();
		users.setuId(userId);

		AccessPermission accessPermission = new AccessPermission();
		accessPermission.setAccessUserId(userId);

		List<Object> al = new ArrayList<>();

		UserDao userDao = new UserDao();

		al.add(userDao.userValidation(users, accessPermission));

		int valideSave = accessPermission.getAccessUpdate();

		if (valideSave == 1) {
			path = customerDao.getAllCustomer(customer);

		} else {
			System.out.println("Invalide Credantials");
		}
		return path;

	}

	public Customer findCustomerById(int CustomerId) {
		return customerDao.findCustomerById(CustomerId);
	}

	public Customer updateDeleteCustomer(Customer customer) throws ClassNotFoundException, SQLException {
		Customer path = null;
		int userId = customer.getUserId();

		User users = new User();
		users.setuId(userId);

		AccessPermission accessPermission = new AccessPermission();
		accessPermission.setAccessUserId(userId);

		List<Object> al = new ArrayList<>();

		UserDao userDao = new UserDao();

		al.add(userDao.userValidation(users, accessPermission));

		int valideSave = accessPermission.getAccessUpdate();

		if (valideSave == 1) {
			path = customerDao.updateDeleteCustomer(customer);

		} else {
			System.out.println("Invalide Credantials");
		}
		return path;
	}

	public Customer updateCustomer(Customer customer) throws ClassNotFoundException, SQLException {
		Customer cust = customerDao.findCustomerById(customer.getCustId());
		cust.setCustId(customer.getCustId());
		cust.setDate(customer.getDate());
		cust.setCustName(customer.getCustName());
		cust.setOwnerName(customer.getOwnerName());
		cust.setOwnerContact(customer.getOwnerContact());
		cust.setPanNo(customer.getPanNo());
		cust.setGstNo(customer.getGstNo());
		cust.setEmail(customer.getEmail());
		cust.setWebsite(customer.getEmail());
		cust.setPhone(customer.getPhone());
		cust.setFax(customer.getFax());
		cust.setInvoiceAddress(customer.getInvoiceAddress());
		cust.setIncome(customer.getIncome());
		cust.setIncomeSource(customer.getIncomeSource());
		cust.setOtherIncome(customer.getOtherIncome());
		cust.setOtherIncomeSource(customer.getOtherIncomeSource());
		cust.setBudget1(customer.getBudget1());
		cust.setBudget2(customer.getBudget2());
		cust.setNotes(customer.getNotes());
		cust.setIsActive(customer.getIsActive());
		cust.setStateId(customer.getStateId());
		cust.setDistId(customer.getDistId());
		cust.setCityId(customer.getCityId());
		cust.setUserId(customer.getUserId());
		cust.setCreatedDate(customer.getCreatedDate());
		cust.setUpdatedBy(customer.getUpdatedBy());
		cust.setUpdatedDate(customer.getUpdatedDate());

		return customerDao.saveCustomer(cust);
	}

	public String deleteCustomerById(int CustomerId) {
		customerDao.deleteCustomerById(CustomerId);
		return "deleted";
	}
}
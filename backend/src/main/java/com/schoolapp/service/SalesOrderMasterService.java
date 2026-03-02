package com.schoolapp.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.schoolapp.dao.SalesOrderMasterDao;
import com.schoolapp.dao.UserDao;
import com.schoolapp.entity.AccessPermission;
import com.schoolapp.entity.SalesOrderMaster;
import com.schoolapp.entity.User;

@Service
public class SalesOrderMasterService {

	@Autowired
	SalesOrderMasterDao salesOrderMasterDao;

	public SalesOrderMaster saveSalesMst(SalesOrderMaster salesOrderMaster) {
		SalesOrderMaster path = null;
		int userId = salesOrderMaster.getUserId();

		User users = new User();
		users.setuId(userId);

		AccessPermission accessPermission = new AccessPermission();
		accessPermission.setAccessUserId(userId);

		List<Object> al = new ArrayList<>();

		UserDao userDao = new UserDao();

		al.add(userDao.userValidation(users, accessPermission));

		int valideSave = accessPermission.getAccessCreate();
		if (valideSave == 1) {
			path = salesOrderMasterDao.saveSalesOrderMaster(salesOrderMaster);

		} else {

			System.out.println("Invalide Credantials");
		}
		return path;

	}

	public SalesOrderMaster updateDeleteSalesOrderMaster(SalesOrderMaster salesOrderMaster) {
		SalesOrderMaster path = null;
		int userId = salesOrderMaster.getUserId();

		User users = new User();
		users.setuId(userId);

		AccessPermission accessPermission = new AccessPermission();
		accessPermission.setAccessUserId(userId);

		List<Object> al = new ArrayList<>();

		UserDao userDao = new UserDao();

		al.add(userDao.userValidation(users, accessPermission));

		int valideSave = accessPermission.getAccessUpdate();

		if (valideSave == 1) {
			path = salesOrderMasterDao.updateDeleteSalesOrderMaster(salesOrderMaster);

		} else {
			System.out.println("Invalide Credantials");
		}
		return path;
	}

	public String getAllSalesMst(SalesOrderMaster salesOrderMaster) {
		return salesOrderMasterDao.getAllSales(salesOrderMaster);
	}

	public SalesOrderMaster findSalesById(int InqId) {

		return salesOrderMasterDao.findSalesById(InqId);
	}

	public SalesOrderMaster updateSales(SalesOrderMaster salesOrderMaster) {

		return salesOrderMasterDao.saveSalesOrderMaster(salesOrderMaster);
	}

	public String deleteSalesById(int InqId) {
		salesOrderMasterDao.deleteSalesByID(InqId);
		return "deleted";
	}
}

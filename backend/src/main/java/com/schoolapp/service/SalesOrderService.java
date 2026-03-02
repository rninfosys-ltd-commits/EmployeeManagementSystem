package com.schoolapp.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.schoolapp.dao.SalesOrderDao;
import com.schoolapp.dao.UserDao;
import com.schoolapp.entity.AccessPermission;
import com.schoolapp.entity.SalesOrder;
import com.schoolapp.entity.User;

@Service
public class SalesOrderService {

	@Autowired
	SalesOrderDao salesOrderDao;

	int existInqMst = 0;

	public List<SalesOrder> saveSalesOrder(List<SalesOrder> salesOrder) {

		List<SalesOrder> path = null;
		int userId = 0;
		int flag = 0;

		for (SalesOrder al : salesOrder) {
			if (flag == 0) {
				flag = 1;
				userId = al.getUserId();
			}
		}

		User users = new User();
		users.setuId(userId);

		AccessPermission accessPermission = new AccessPermission();
		accessPermission.setAccessUserId(userId);

		List<Object> al = new ArrayList<>();

		UserDao userDao = new UserDao();

		al.add(userDao.userValidation(users, accessPermission));

		int valideSave = accessPermission.getAccessCreate();
		if (valideSave == 1) {
			path = salesOrderDao.saveSalesOrder(salesOrder);

		} else {
			System.out.println("Invalide Credantials");
		}
		return path;

	}

	public SalesOrder updateDeleteSalesOrder(SalesOrder salesOrder) {
		SalesOrder path = null;
		int userId = salesOrder.getUserId();

		User users = new User();
		users.setuId(userId);

		AccessPermission accessPermission = new AccessPermission();
		accessPermission.setAccessUserId(userId);

		List<Object> al = new ArrayList<>();

		UserDao userDao = new UserDao();

		al.add(userDao.userValidation(users, accessPermission));

		int valideSave = accessPermission.getAccessUpdate();

		if (valideSave == 1) {
			path = salesOrderDao.updateDeleteSalesOrder(salesOrder);

		} else {
			System.out.println("Invalide Credantials");
		}
		return path;
	}

	public List<SalesOrder> getAllSalesOrder() {
		return salesOrderDao.getAllSalesOrder();
	}

	public SalesOrder findSalesOrderById(int salesOrderId) {
		return salesOrderDao.findSalesOrderById(salesOrderId);
	}

	public SalesOrder updateSalesOrder(SalesOrder salesOrder) {
		SalesOrder salesD = salesOrderDao.findSalesOrderById(salesOrder.getSalesOrderId());
		salesD.setSalesDate(salesOrder.getSalesDate());
		salesD.setCustomerId(salesOrder.getCustomerId());
		salesD.setSalesOrderId(salesOrder.getSalesOrderId());
		salesD.setProductId(salesOrder.getProductId());
		salesD.setPartiCulars(salesOrder.getPartiCulars());
		salesD.setRate(salesOrder.getRate());
		salesD.setQuantity(salesOrder.getQuantity());
		salesD.setDiscount(salesOrder.getDiscount());
		salesD.setAmount(salesOrder.getAmount());
		salesD.setTotal(salesOrder.getTotal());
		salesD.setGrandTotal(salesOrder.getGrandTotal());
		salesD.setMrp(salesOrder.getMrp());
		salesD.setScheme(salesOrder.getScheme());
		salesD.setCgst(salesOrder.getCgst());
		salesD.setSgst(salesOrder.getSgst());
		salesD.setCgstPer(salesOrder.getCgstPer());
		salesD.setSgstPer(salesOrder.getSgstPer());
		salesD.setIgst(salesOrder.getIgst());
		salesD.setIgstPer(salesOrder.getIgstPer());
		salesD.setDcn(salesOrder.getDcn());
		salesD.setUserId(salesOrder.getUserId());
		salesD.setOrgId(salesOrder.getOrgId());
		salesD.setBranchId(salesOrder.getBranchId());
		salesD.setCreatedDate(salesOrder.getCreatedDate());
		salesD.setUpdatedBy(salesOrder.getUpdatedBy());
		salesD.setUpdatedDate(salesOrder.getUpdatedDate());
		salesD.setIsActive(salesOrder.getIsActive());

		return salesOrderDao.updateDeleteSalesOrder(salesD);
	}

	public String deleteSalesOrderById(int salesOrderId) {
		salesOrderDao.deleteSalesOrderById(salesOrderId);
		return "deleted";
	}
}

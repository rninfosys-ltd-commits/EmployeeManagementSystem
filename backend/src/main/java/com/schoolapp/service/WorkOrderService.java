package com.schoolapp.service;

import java.sql.SQLException;
import java.util.ArrayList;
// import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.schoolapp.dao.UserDao;
import com.schoolapp.dao.WorkOrderDao;
import com.schoolapp.dao.WorkOrderMasterDao;
import com.schoolapp.entity.AccessPermission;
import com.schoolapp.entity.User;
// import com.schoolapp.entity.WorkCompletion;
import com.schoolapp.entity.WorkOrder;

@Service
public class WorkOrderService {
	@Autowired
	WorkOrderDao workOrderDao;
	WorkOrderMasterDao workOrderMasterDao;

	public List<WorkOrder> saveWorkOrder(List<WorkOrder> workOrder) throws ClassNotFoundException, SQLException {

		List<WorkOrder> path = null;
		int userId = 0;
		int flag = 0;

		for (WorkOrder al : workOrder) {
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

			path = workOrderDao.saveWorkOrder(workOrder);

		}
		return path;

	}

	public String updateWorkOrder(WorkOrder workOrder) throws ClassNotFoundException, SQLException {
		String path = null;
		int userId = workOrder.getUserId();

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
			path = workOrderDao.updateWorkOrder(workOrder);

		} else {
			path = "Invalide Credantials";
		}
		return path;
	}

	public String getWorkOrderContractor(WorkOrder workOrder) throws Exception {
		String path = null;
		int userId = workOrder.getUserId();

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
			path = workOrderDao.getWorkOrderContractor();

		} else {
			path = "Invalide Credantials";
		}
		return path;
	}

	public String getWorkOrderProduct(WorkOrder workOrder) throws Exception {
		String path = null;
		int userId = workOrder.getUserId();

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
			path = workOrderDao.getWorkOrderProduct(workOrder);

		} else {
			path = "Invalide Credantials";
		}
		return path;

	}

	public String getWorkOrderArea(WorkOrder workOrder) throws Exception {
		return workOrderDao.getWorkOrderArea(workOrder);
	}

	public WorkOrder updateDeleteWorkOrder(WorkOrder workOrder) throws ClassNotFoundException, SQLException {
		WorkOrder path = null;
		int userId = workOrder.getUserId();

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
			path = workOrderDao.updateDeleteWorkOrder(workOrder);

		}
		return path;
	}

	public String getAllWorkOrder() throws Exception {
		return workOrderDao.getAllWorkOrder();
	}

	public String getAllWorkOrderByOrderId(int id) throws Exception {
		return workOrderDao.getAllWorkOrderByOrderId(id);
	}

	public WorkOrder findWorkOrderById(int WorkOrderId) {
		return workOrderDao.findWorkOrderById(WorkOrderId);
	}

	public String deleteWorkOrderById(int workOrderId) {
		workOrderDao.deleteWorkOrderById(workOrderId);
		return "deleted";
	}
}

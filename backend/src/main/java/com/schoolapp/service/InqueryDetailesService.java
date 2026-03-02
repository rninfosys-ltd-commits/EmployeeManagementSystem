package com.schoolapp.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.schoolapp.dao.InqueryDetailesDao;
import com.schoolapp.dao.UserDao;
import com.schoolapp.entity.AccessPermission;
import com.schoolapp.entity.InqueryDetailes;
import com.schoolapp.entity.User;

@Service
public class InqueryDetailesService {
	@Autowired
	InqueryDetailesDao inqueryDetailesDao;

	int existInqMst = 0;

	public InqueryDetailes saveInqueryDetailes(InqueryDetailes inqueryDetailes) {

		InqueryDetailes path = null;
		int userId = inqueryDetailes.getUserId();

		User users = new User();
		users.setuId(userId);

		AccessPermission accessPermission = new AccessPermission();
		accessPermission.setAccessUserId(userId);

		List<Object> al = new ArrayList<>();

		UserDao userDao = new UserDao();

		al.add(userDao.userValidation(users, accessPermission));

		int valideSave = accessPermission.getAccessCreate();
		if (valideSave == 1) {
			path = inqueryDetailesDao.saveInqueryDetailes(inqueryDetailes);

		} else {
			System.out.println("Invalide Credantials");
		}
		return path;

	}

	public InqueryDetailes updateDeleteInqueryDetailes(InqueryDetailes inqueryDetailes) {
		InqueryDetailes path = null;
		int userId = inqueryDetailes.getUserId();

		User users = new User();
		users.setuId(userId);

		AccessPermission accessPermission = new AccessPermission();
		accessPermission.setAccessUserId(userId);

		List<Object> al = new ArrayList<>();

		UserDao userDao = new UserDao();

		al.add(userDao.userValidation(users, accessPermission));

		int valideSave = accessPermission.getAccessUpdate();

		if (valideSave == 1) {
			path = inqueryDetailesDao.updateDeleteInqueryDetailes(inqueryDetailes);

		} else {
			System.out.println("Invalide Credantials");
		}
		return path;
	}

	public List<InqueryDetailes> getAllInqueryDetailes() {
		return inqueryDetailesDao.getAllInqueryDetailes();
	}

	public InqueryDetailes findInqueryDetailesById(int inqueryDetailesId) {
		return inqueryDetailesDao.findInqueryDetailesById(inqueryDetailesId);
	}

	public InqueryDetailes updateInqueryDetailes(InqueryDetailes inqueryDetailes) {
		Integer inqueryDetailesId = inqueryDetailes.getInqueryDetailesId();
		InqueryDetailes inqueryD = inqueryDetailesDao.findInqueryDetailesById(inqueryDetailesId);
		inqueryD.setInqueryDate(inqueryDetailes.getInqueryDate());
		inqueryD.setLeadAccountId(inqueryDetailes.getLeadAccountId());
		inqueryD.setInqueryDetailesId(inqueryDetailes.getInqueryDetailesId());
		inqueryD.setProductCode(inqueryDetailes.getProductCode());
		inqueryD.setPartiCulars(inqueryDetailes.getPartiCulars());
		inqueryD.setRate(inqueryDetailes.getRate());
		inqueryD.setQuantity(inqueryDetailes.getQuantity());
		inqueryD.setDiscount(inqueryDetailes.getDiscount());
		inqueryD.setAmount(inqueryDetailes.getAmount());
		inqueryD.setTotal(inqueryDetailes.getTotal());
		inqueryD.setGrandTotal(inqueryDetailes.getGrandTotal());
		inqueryD.setMrp(inqueryDetailes.getMrp());
		inqueryD.setScheme(inqueryDetailes.getScheme());
		inqueryD.setCgst(inqueryDetailes.getCgst());
		inqueryD.setSgst(inqueryDetailes.getSgst());
		inqueryD.setCgstPer(inqueryDetailes.getCgstPer());
		inqueryD.setSgstPer(inqueryDetailes.getSgstPer());
		inqueryD.setIgst(inqueryDetailes.getIgst());
		inqueryD.setIgstPer(inqueryDetailes.getIgstPer());
		inqueryD.setDcn(inqueryDetailes.getDcn());
		inqueryD.setUserId(inqueryDetailes.getUserId());
		inqueryD.setOrgId(inqueryDetailes.getOrgId());
		inqueryD.setBranchId(inqueryDetailes.getBranchId());
		inqueryD.setCreatedDate(inqueryDetailes.getCreatedDate());
		inqueryD.setUpdatedBy(inqueryDetailes.getUpdatedBy());
		inqueryD.setUpdatedDate(inqueryDetailes.getUpdatedDate());
		inqueryD.setIsActive(inqueryDetailes.getIsActive());

		return inqueryDetailesDao.saveInqueryDetailes(inqueryD);
	}

	public String deleteInqueryDetailesById(int inqueryDetailesId) {
		inqueryDetailesDao.deleteInqueryDetailesById(inqueryDetailesId);
		return "deleted";
	}
}

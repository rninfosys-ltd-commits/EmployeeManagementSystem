package com.schoolapp.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.schoolapp.dao.QuotationDao;
import com.schoolapp.dao.UserDao;
import com.schoolapp.entity.AccessPermission;
import com.schoolapp.entity.Quotation;
import com.schoolapp.entity.User;

@Service
public class QuotationService {

	@Autowired
	QuotationDao quotationDao;

	int existInqMst = 0;

	public List<Quotation> saveQuotation(List<Quotation> quotation) {

		List<Quotation> path = null;
		int userId = 0;
		int flag = 0;

		for (Quotation al : quotation) {
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
			path = quotationDao.saveQuotation(quotation);

		} else {
			System.out.println("Invalide Credantials");
		}
		return path;

	}

	public Quotation updateDeleteQuotation(Quotation quotation) {
		Quotation path = null;
		int userId = quotation.getUserId();

		User users = new User();
		users.setuId(userId);

		AccessPermission accessPermission = new AccessPermission();
		accessPermission.setAccessUserId(userId);

		List<Object> al = new ArrayList<>();

		UserDao userDao = new UserDao();

		al.add(userDao.userValidation(users, accessPermission));

		int valideSave = accessPermission.getAccessUpdate();

		if (valideSave == 1) {
			path = quotationDao.updateDeleteQuotation(quotation);

		} else {
			System.out.println("Invalide Credantials");
		}
		return path;
	}

	public List<Quotation> getAllQuotation() {
		return quotationDao.getAllQuotation();
	}

	public Quotation findQuotationById(int quotationId) {
		return quotationDao.findQuotationById(quotationId);
	}

	public Quotation updateQuotation(Quotation quotation) {
		Quotation quotationD = quotationDao.findQuotationById(quotation.getQuotationId());
		quotationD.setQuotationDate(quotation.getQuotationDate());
		quotationD.setCustomerId(quotation.getCustomerId());
		quotationD.setQuotationId(quotation.getQuotationId());
		quotationD.setProductId(quotation.getProductId());
		quotationD.setPartiCulars(quotation.getPartiCulars());
		quotationD.setRate(quotation.getRate());
		quotationD.setQuantity(quotation.getQuantity());
		quotationD.setDiscount(quotation.getDiscount());
		quotationD.setAmount(quotation.getAmount());
		quotationD.setTotal(quotation.getTotal());
		quotationD.setGrandTotal(quotation.getGrandTotal());
		quotationD.setMrp(quotation.getMrp());
		quotationD.setScheme(quotation.getScheme());
		quotationD.setCgst(quotation.getCgst());
		quotationD.setSgst(quotation.getSgst());
		quotationD.setCgstPer(quotation.getCgstPer());
		quotationD.setSgstPer(quotation.getSgstPer());
		quotationD.setIgst(quotation.getIgst());
		quotationD.setIgstPer(quotation.getIgstPer());
		quotationD.setUserId(quotation.getUserId());
		quotationD.setOrgId(quotation.getOrgId());
		quotationD.setBranchId(quotation.getBranchId());
		quotationD.setCreatedDate(quotation.getCreatedDate());
		quotationD.setUpdatedBy(quotation.getUpdatedBy());
		quotationD.setUpdatedDate(quotation.getUpdatedDate());
		quotationD.setIsActive(quotation.getIsActive());

		return quotationDao.updateDeleteQuotation(quotationD);
	}

	public String deleteQuotationById(int quotationId) {
		quotationDao.deleteQuotationById(quotationId);
		return "deleted";
	}
}

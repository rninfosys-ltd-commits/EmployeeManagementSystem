package com.schoolapp.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.schoolapp.dao.OrganizationDao;
import com.schoolapp.dao.UserDao;
import com.schoolapp.entity.AccessPermission;
import com.schoolapp.entity.Organization;
import com.schoolapp.entity.User;

@Service
public class OrgnazationService {

	@Autowired
	OrganizationDao organizationDao;

	public Organization saveOrganization(Organization organization) {

		Organization path = null;
		int userId = organization.getUserId();

		User users = new User();
		users.setuId(userId);

		AccessPermission accessPermission = new AccessPermission();
		accessPermission.setAccessUserId(userId);

		List<Object> al = new ArrayList<>();

		UserDao userDao = new UserDao();

		al.add(userDao.userValidation(users, accessPermission));

		int valideSave = accessPermission.getAccessCreate();
		if (valideSave == 1) {
			path = organizationDao.saveOrganization(organization);

		} else {
			System.out.println("Invalide Credantials");
		}
		return path;
	}

	public List<Organization> getAllOrganizations() {
		return organizationDao.getAllOrganizations();
	}

	public Organization updateDeleteOrganization(Organization organization) {
		Organization path = null;
		int userId = organization.getUserId();

		User users = new User();
		users.setuId(userId);

		AccessPermission accessPermission = new AccessPermission();
		accessPermission.setAccessUserId(userId);

		List<Object> al = new ArrayList<>();

		UserDao userDao = new UserDao();

		al.add(userDao.userValidation(users, accessPermission));

		int valideSave = accessPermission.getAccessUpdate();

		if (valideSave == 1) {
			path = organizationDao.updateDeleteOrganization(organization);

		} else {
			System.out.println("Invalide Credantials");
		}
		return path;
	}

	public Organization findOrganizationById(int OrganizationId) {
		return organizationDao.findOrganizationById(OrganizationId);
	}

	public Organization updateOrganization(Organization organization) {
		Organization org = organizationDao.findOrganizationById(organization.getOrgId());
		org.setOrgId(organization.getOrgId());
		org.setDate(organization.getDate());
		org.setOrgName(organization.getOrgName());
		org.setOwnerName(organization.getOwnerName());
		org.setOwnerContact(organization.getOwnerContact());
		org.setPanNo(organization.getPanNo());
		org.setGstNo(organization.getGstNo());
		org.setEmail(organization.getEmail());
		org.setWebsite(organization.getEmail());
		org.setPhone(organization.getPhone());
		org.setFax(organization.getFax());
		org.setInvoiceAddress(organization.getInvoiceAddress());
		org.setIncome(organization.getIncome());
		org.setIncomeSource(organization.getIncomeSource());
		org.setOtherIncome(organization.getOtherIncome());
		org.setOtherIncomeSource(organization.getOtherIncomeSource());
		org.setBudget1(organization.getBudget1());
		org.setBudget2(organization.getBudget2());
		org.setNotes(organization.getNotes());
		org.setIsActive(organization.getIsActive());
		org.setStateId(organization.getStateId());
		org.setDistId(organization.getDistId());
		org.setCityId(organization.getCityId());
		org.setUserId(organization.getUserId());
		org.setCreatedDate(organization.getCreatedDate());
		org.setUpdatedBy(organization.getUpdatedBy());
		org.setUpdatedDate(organization.getUpdatedDate());
		org.setCustomerId(organization.getCustomerId());

		return organizationDao.saveOrganization(org);
	}

	public String deleteOrganizationById(int OrgId) {
		organizationDao.deleteOrgnizationById(OrgId);
		return "deleted";
	}
}

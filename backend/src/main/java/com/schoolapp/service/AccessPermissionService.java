package com.schoolapp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.schoolapp.dao.AccessPermissionDao;
import com.schoolapp.entity.AccessPermission;

@Service
public class AccessPermissionService {
	@Autowired
	AccessPermissionDao accessPermissionDao;

	public AccessPermission saveAccessPermission(AccessPermission accessPermission) {
		return accessPermissionDao.saveAccessPermission(accessPermission);
	}

	public List<AccessPermission> getAllAccessPermission() throws Exception {
		return accessPermissionDao.getAllAccessPermission();
	}

	public AccessPermission findAccessPermissionById(int AccessPermissionId) {
		return accessPermissionDao.findAccessPermissionById(AccessPermissionId);
	}

	public AccessPermission updateAccessPermission(AccessPermission accessPermission) {
		AccessPermission accounts = accessPermissionDao
				.findAccessPermissionById(accessPermission.getAccessPermissionId());

		accounts.setAccessUserId(accessPermission.getAccessUserId());
		accounts.setModelId(accessPermission.getModelId());
		accounts.setAccessCreate(accessPermission.getAccessCreate());
		accounts.setAccessRead(accessPermission.getAccessRead());
		accounts.setAccessUpdate(accessPermission.getAccessUpdate());
		accounts.setAccessDelete(accessPermission.getAccessDelete());
		accounts.setUserId(accessPermission.getUserId());
		accounts.setOrgId(accessPermission.getOrgId());
		accounts.setBranchId(accessPermission.getBranchId());
		accounts.setCreatedDate(accessPermission.getCreatedDate());
		accounts.setUpdatedBy(accessPermission.getUpdatedBy());
		accounts.setUpdatedDate(accessPermission.getUpdatedDate());
		accounts.setIsActive(accessPermission.getIsActive());

		return accessPermissionDao.saveAccessPermission(accounts);
	}

	public String deleteAccessPermissionById(int accessPermissionId) {
		accessPermissionDao.deleteAccessPermissionById(accessPermissionId);
		return "deleted";
	}
}

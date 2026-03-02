package com.schoolapp.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.schoolapp.entity.AccessPermission;
import com.schoolapp.repository.AccessPermissionRepo;

@Component
public class AccessPermissionDao {
	@Autowired
	AccessPermissionRepo accessPermissionRepo;

	public AccessPermission saveAccessPermission(AccessPermission accessPermission) {
		return accessPermissionRepo.save(accessPermission);
	}

	public List<AccessPermission> getAllAccessPermission() throws Exception {
		return accessPermissionRepo.findAll();
	}

	public AccessPermission findAccessPermissionById(int AccessPermissionId) {
		return (AccessPermission) accessPermissionRepo.findById(AccessPermissionId).get();
	}

	public String deleteAccessPermissionById(int AccessPermissionId) {
		accessPermissionRepo.deleteById(AccessPermissionId);
		return "deleted";
	}
}

package com.schoolapp.controller;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.schoolapp.entity.AccessPermission;
import com.schoolapp.service.AccessPermissionService;

@RestController
@RequestMapping("/accessPermission")

public class AccessPermissionController {
	@Autowired
	AccessPermissionService accessPermissionService;

	@PostMapping("/save")
	public String saveAccessPermission(@RequestBody AccessPermission accessPermission)
			throws ClassNotFoundException, SQLException {
		accessPermissionService.saveAccessPermission(accessPermission);
		return "Record save successfully";
	}

	@GetMapping("/getAll")
	public List<AccessPermission> getAllAccessPermission() throws Exception {
		return accessPermissionService.getAllAccessPermission();
	}

	// SELECT
	// u.u_id,u.user_name,u.email,ap.access_create,ap.access_read,ap.access_delete,ap.access_update
	// FROM crmdb.access_permission ap
	// inner join user u on ap.access_user_id=u.u_id
	// where u.is_active=1 and (u.u_id=4 or u.email="abc@123" and u.password="abc")
	// ;

	@GetMapping("/get")
	public AccessPermission findWorkOrderById(@RequestBody AccessPermission accessPermission) {
		return accessPermissionService.findAccessPermissionById(accessPermission.getAccessPermissionId());
	}

	@PutMapping("/update")
	public String updateAccessPermission(@RequestBody AccessPermission accessPermission) {
		accessPermissionService.updateAccessPermission(accessPermission);
		return "Record Updated.....";
	}

	// http://192.168.43.146:8080/leadAccounts/getAll
	@DeleteMapping("/delete")
	public String deleteAccessPermissionById(@RequestBody AccessPermission accessPermission) {
		int id = accessPermission.getAccessPermissionId();

		if (id > 0) {
			accessPermissionService.deleteAccessPermissionById(id);
			return "deleted....." + id;
		}

		return "Wrong ID" + id;
	}
}

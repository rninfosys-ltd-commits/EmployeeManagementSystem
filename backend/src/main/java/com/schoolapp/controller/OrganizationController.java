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

import com.schoolapp.entity.Organization;
import com.schoolapp.service.OrgnazationService;

@RestController
@RequestMapping("/organization")

public class OrganizationController {

	@Autowired
	OrgnazationService organizationService;

	@PostMapping("/save")
	public Organization saveLeadAccounts(@RequestBody Organization organization)
			throws ClassNotFoundException, SQLException {

		return organizationService.saveOrganization(organization);
	}

	// getall
	@GetMapping("/getAll")
	public List<Organization> getAllLeadAccounts() {
		return organizationService.getAllOrganizations();
	}

	//
	@GetMapping("/get")
	public Organization findLeadAccountsById(@RequestBody Organization organization) {

		return organizationService.findOrganizationById(organization.getOrgId());
		// return State;
	}

	@PutMapping("/updateDeleteOrganization")
	public String updateDeleteOrganization(@RequestBody Organization organization)
			throws ClassNotFoundException, SQLException {
		organizationService.updateDeleteOrganization(organization);
		return "Record Updated.....";
	}

	@PutMapping("/update")
	public String updateLeadAccounts(@RequestBody Organization organization)
			throws ClassNotFoundException, SQLException {
		organizationService.updateOrganization(organization);
		return "Record Updated.....";
	}

	// http://192.168.43.146:8080/leadAccounts/getAll
	@DeleteMapping("/delete")
	public String deleteLeadAccountsById(@RequestBody Organization organization) {
		int id = organization.getOrgId();

		if (id > 0) {
			organizationService.deleteOrganizationById(id);
			return "deleted....." + id;
		}

		return "Wrong ID" + id;
	}
}

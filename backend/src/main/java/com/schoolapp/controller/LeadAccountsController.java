package com.schoolapp.controller;

import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.schoolapp.entity.LeadAccounts;
import com.schoolapp.service.LeadAccountsService;

@RestController
@RequestMapping("/leadAccounts")

public class LeadAccountsController {

	@Autowired
	LeadAccountsService leadAccountsService;

	@PostMapping("/save")
	public String saveLeadAccounts(@RequestBody LeadAccounts leadAccounts) throws ClassNotFoundException, SQLException {
		leadAccountsService.saveLeadAccounts(leadAccounts);
		return "Record save successfully";
	}

	@GetMapping("/getAll")
	public String getAllLeadAccounts() throws Exception {
		return leadAccountsService.getAllLeadAccounts();
	}

	//
	@GetMapping("/get")
	public LeadAccounts findLeadAccountsById(@RequestBody LeadAccounts leadAccounts) {

		return leadAccountsService.findLeadAccountsById(leadAccounts.getLeadId());
		// return State;
	}

	@PutMapping("/updateDeleteLeadAccounts")
	public String updateDeleteLeadAccounts(@RequestBody LeadAccounts leadAccounts)
			throws ClassNotFoundException, SQLException {
		leadAccountsService.updateDeleteLeadAccounts(leadAccounts);
		return "Record Updated.....";
	}

	@PutMapping("/update")
	public String updateLeadAccounts(@RequestBody LeadAccounts leadAccounts)
			throws ClassNotFoundException, SQLException {
		leadAccountsService.updateLeadAccounts(leadAccounts);
		return "Record Updated.....";
	}

	// http://192.168.43.146:8080/leadAccounts/getAll
	@DeleteMapping("/delete")
	public String deleteLeadAccountsById(@RequestBody LeadAccounts leadAccounts) {
		int id = leadAccounts.getLeadId();

		if (id > 0) {
			leadAccountsService.deleteLeadAccountsById(id);
			return "deleted....." + id;
		}

		return "Wrong ID" + id;
	}

}

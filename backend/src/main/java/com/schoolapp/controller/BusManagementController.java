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

import com.schoolapp.entity.BusManagement;

@RestController
@RequestMapping("/busManagement")

public class BusManagementController {
	@Autowired
	com.schoolapp.service.BusManagementService BusManagementService;

	@PostMapping("/save")
	public String saveBusManagement(@RequestBody BusManagement BusManagement) {
		BusManagementService.saveBusManagement(BusManagement);
		return "Record save successfully";
	}

	@PostMapping("/getAll")
	public String getAllBusManagement(@RequestBody BusManagement BusManagement)
			throws ClassNotFoundException, SQLException {
		return BusManagementService.getAllBusManagement(BusManagement);
	}

	@GetMapping("/get")
	public BusManagement findBusManagementById(@RequestBody BusManagement BusManagement) {

		return BusManagementService.findBusManagementById(BusManagement.getBusManagementId());

	}

	@PutMapping("/update")
	public String updateBusManagement(@RequestBody BusManagement BusManagement) {
		BusManagementService.updateBusManagement(BusManagement);
		return "Record Updated.....";
	}

	@DeleteMapping("/delete")
	public String deleteBusManagementById(@RequestBody BusManagement BusManagement) {
		int id = BusManagement.getBusManagementId();
		if (id > 0) {
			BusManagementService.deleteBusManagementById(id);
			return "deleted..." + id;
		}

		return "Wrong Id" + id;
	}
}

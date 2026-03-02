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

import com.schoolapp.entity.WorkOrderMaster;
import com.schoolapp.service.WorkOrderMasterService;

@RestController
@RequestMapping("/workOrderMaster")

public class WorkOrderMasterController {
	@Autowired
	WorkOrderMasterService workOrderMasterService;

	@PostMapping("/save")
	public String saveWorkOrderMaster(@RequestBody WorkOrderMaster workOrderMaster)
			throws ClassNotFoundException, SQLException {
		workOrderMasterService.saveWorkOrderMaster(workOrderMaster);
		return "Record save successfully";
	}

	@GetMapping("/getAll")
	public String getAllWorkOrderMaster() throws Exception {
		return workOrderMasterService.getAllWorkOrderMaster();
	}

	//
	@GetMapping("/get")
	public WorkOrderMaster findWorkOrderMasterById(@RequestBody WorkOrderMaster workOrderMaster) {

		return workOrderMasterService.findWorkOrderMasterById(workOrderMaster.getWorkOrderMasterId());
		// return State;
	}

	@PutMapping("/update")
	public String updateWorkOrderMaster(@RequestBody WorkOrderMaster workOrderMaster)
			throws ClassNotFoundException, SQLException {
		workOrderMasterService.updateWorkOrderMaster(workOrderMaster);
		return "Record Updated.....";
	}

	@PutMapping("/updateDeleteWorkOrderMaster")
	public String updateDeleteWorkOrderMaster(@RequestBody WorkOrderMaster workOrderMaster)
			throws ClassNotFoundException, SQLException {
		workOrderMasterService.updateDeleteWorkOrderMaster(workOrderMaster);
		return "Record Updated.....";
	}

	@DeleteMapping("/delete")
	public String deleteWorkOrderMasterById(@RequestBody WorkOrderMaster workOrderMaster) {
		int id = workOrderMaster.getWorkOrderMasterId();

		if (id > 0) {
			workOrderMasterService.deleteWorkOrderMasterById(id);
			return "deleted....." + id;
		}

		return "Wrong ID" + id;
	}
}

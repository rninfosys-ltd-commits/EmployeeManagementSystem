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

import com.schoolapp.entity.AreaMaster;
import com.schoolapp.service.AreaMasterService;

@RestController
@RequestMapping("/areaMaster")

public class AreaMasterController {

	@Autowired
	AreaMasterService areaMasterService;

	@PostMapping("/save")
	public String saveWorkOrder(@RequestBody AreaMaster areaMaster) throws ClassNotFoundException, SQLException {
		areaMasterService.saveAreaMaster(areaMaster);
		return "Record save successfully";
	}

	@GetMapping("/getAll")
	public List<AreaMaster> getAllAreaMaster() throws Exception {
		return areaMasterService.getAllAreaMaster();
	}

	//
	@GetMapping("/get")
	public AreaMaster findAreaMasterById(@RequestBody AreaMaster areaMaster) {

		return areaMasterService.findAreaMasterById(areaMaster.getAreaMasterId());
		// return State;
	}

	@PutMapping("/updateDeleteAreaMaster")
	public String updateDeleteAreaMaster(@RequestBody AreaMaster areaMaster)
			throws ClassNotFoundException, SQLException {
		areaMasterService.updateDeleteAreaMaster(areaMaster);
		return "Record Updated.....";
	}

	@PutMapping("/update")
	public String updateAreaMaster(@RequestBody AreaMaster areaMaster) throws ClassNotFoundException, SQLException {
		areaMasterService.updateAreaMaster(areaMaster);
		return "Record Updated.....";
	}

	// http://192.168.43.146:8080/leadAccounts/getAll
	@DeleteMapping("/delete")
	public String deleteAreaMasterById(@RequestBody AreaMaster areaMaster) {
		int id = areaMaster.getAreaMasterId();

		if (id > 0) {
			areaMasterService.deleteAreaMasterById(id);
			return "deleted....." + id;
		}

		return "Wrong ID" + id;
	}
}

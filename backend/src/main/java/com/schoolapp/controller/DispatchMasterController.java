package com.schoolapp.controller;

import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.schoolapp.entity.DispatchMaster;
import com.schoolapp.service.DispatchMasterService;

@RestController
@RequestMapping("/DispatchMaster")

public class DispatchMasterController {

	@Autowired
	DispatchMasterService DispatchMasterService;

	@PostMapping("/save")
	public String saveDispatchMaster(@RequestBody DispatchMaster DispatchMaster)
			throws ClassNotFoundException, SQLException {
		DispatchMasterService.saveDispatchMaster(DispatchMaster);
		return "Record save successfully";
	}

	@PostMapping("/getAll")
	public String getAllDispatchMaster(@RequestBody DispatchMaster DispatchMaster) throws Exception {

		return DispatchMasterService.getAllDispatchMaster(DispatchMaster);
	}

	@PostMapping("/getAllStudentClassWise")
	public String getAllStudentClassWise(@RequestBody DispatchMaster DispatchMaster) throws Exception {
		return DispatchMasterService.getAllStudentClassWise(DispatchMaster);
	}

	@GetMapping("/get")
	public DispatchMaster findDispatchMasterById(@PathVariable int DispatchMasterID) {
		return DispatchMasterService.findDispatchMasterById(DispatchMasterID);
	}

	@PutMapping("/update")
	public DispatchMaster updateDeleteDispatchMaster(@RequestBody DispatchMaster DispatchMaster)
			throws ClassNotFoundException, SQLException {
		return DispatchMasterService.updateDeleteDispatchMaster(DispatchMaster);
	}

	@DeleteMapping("/delete")
	public String deleteDispatchMasterById(@RequestBody DispatchMaster DispatchMaster) {
		DispatchMasterService.deleteDispatchMasterById(DispatchMaster.getDispatchMasterId());
		return "deleted............";
	}
}

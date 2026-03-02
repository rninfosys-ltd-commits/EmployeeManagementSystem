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

import com.schoolapp.entity.OutWorkMaterialMaster;
import com.schoolapp.service.OutWorkMaterialMasterService;

@RestController
@RequestMapping("/OutWorkMaterialMaster")

public class OutWorkMaterialMasterController {

	@Autowired
	OutWorkMaterialMasterService OutWorkMaterialMasterService;

	@PostMapping("/save")
	public String saveOutWorkMaterialMaster(@RequestBody OutWorkMaterialMaster OutWorkMaterialMaster)
			throws ClassNotFoundException, SQLException {
		OutWorkMaterialMasterService.saveOutWorkMaterialMaster(OutWorkMaterialMaster);
		return "Record save successfully";
	}

	@PostMapping("/getAll")
	public String getAllOutWorkMaterialMaster(@RequestBody OutWorkMaterialMaster OutWorkMaterialMaster)
			throws Exception {

		return OutWorkMaterialMasterService.getAllOutWorkMaterialMaster(OutWorkMaterialMaster);
	}

	@PostMapping("/getAllStudentClassWise")
	public String getAllStudentClassWise(@RequestBody OutWorkMaterialMaster OutWorkMaterialMaster) throws Exception {
		return OutWorkMaterialMasterService.getAllStudentClassWise(OutWorkMaterialMaster);
	}

	@GetMapping("/get")
	public OutWorkMaterialMaster findOutWorkMaterialMasterById(@PathVariable int OutWorkMaterialMasterID) {
		return OutWorkMaterialMasterService.findOutWorkMaterialMasterById(OutWorkMaterialMasterID);
	}

	@PutMapping("/update")
	public OutWorkMaterialMaster updateDeleteOutWorkMaterialMaster(
			@RequestBody OutWorkMaterialMaster OutWorkMaterialMaster) throws ClassNotFoundException, SQLException {
		return OutWorkMaterialMasterService.updateDeleteOutWorkMaterialMaster(OutWorkMaterialMaster);
	}

	@DeleteMapping("/delete")
	public String deleteOutWorkMaterialMasterById(@RequestBody OutWorkMaterialMaster OutWorkMaterialMaster) {
		OutWorkMaterialMasterService
				.deleteOutWorkMaterialMasterById(OutWorkMaterialMaster.getOutWorkMaterialMasterId());
		return "deleted............";
	}
}

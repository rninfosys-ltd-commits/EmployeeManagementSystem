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

import com.schoolapp.entity.GRNEntryMaster;
import com.schoolapp.service.GRNEntryMasterService;

@RestController
@RequestMapping("/GRNEntryMaster")

public class GRNEntryMasterController {
	@Autowired
	GRNEntryMasterService GRNEntryMasterService;

	@PostMapping("/save")
	public String saveGRNEntryMaster(@RequestBody GRNEntryMaster GRNEntryMaster)
			throws ClassNotFoundException, SQLException {
		GRNEntryMasterService.saveGRNEntryMaster(GRNEntryMaster);
		return "Record save successfully";
	}

	@PostMapping("/getAll")
	public String getAllGRNEntryMaster(@RequestBody GRNEntryMaster GRNEntryMaster) throws Exception {

		return GRNEntryMasterService.getAllGRNEntryMaster(GRNEntryMaster);
	}

	@PostMapping("/getAllStudentClassWise")
	public String getAllStudentClassWise(@RequestBody GRNEntryMaster GRNEntryMaster) throws Exception {
		return GRNEntryMasterService.getAllStudentClassWise(GRNEntryMaster);
	}

	@GetMapping("/get")
	public GRNEntryMaster findGRNEntryMasterById(@PathVariable int GRNEntryMasterID) {
		return GRNEntryMasterService.findGRNEntryMasterById(GRNEntryMasterID);
	}

	@PutMapping("/update")
	public GRNEntryMaster updateDeleteGRNEntryMaster(@RequestBody GRNEntryMaster GRNEntryMaster)
			throws ClassNotFoundException, SQLException {
		return GRNEntryMasterService.updateDeleteGRNEntryMaster(GRNEntryMaster);
	}

	@DeleteMapping("/delete")
	public String deleteGRNEntryMasterById(@RequestBody GRNEntryMaster GRNEntryMaster) {
		GRNEntryMasterService.deleteGRNEntryMasterById(GRNEntryMaster.getGRNEntryMasterId());
		return "deleted............";
	}
}

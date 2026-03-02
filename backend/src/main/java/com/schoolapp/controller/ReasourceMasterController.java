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

import com.schoolapp.entity.ReasourceMaster;
import com.schoolapp.service.ReasourceMasterService;

@RestController
@RequestMapping("/reasourceMaster")

public class ReasourceMasterController {
	@Autowired
	ReasourceMasterService ReasourceMasterService;

	@PostMapping("/save")
	public String saveReasourceMaster(@RequestBody ReasourceMaster ReasourceMaster)
			throws ClassNotFoundException, SQLException {
		ReasourceMasterService.saveReasourceMaster(ReasourceMaster);
		return "Record save successfully";
	}

	@PostMapping("/getAll")
	public String getAllReasourceMaster(@RequestBody ReasourceMaster ReasourceMaster)
			throws ClassNotFoundException, SQLException {
		return ReasourceMasterService.getAllReasourceMaster(ReasourceMaster);
	}

	@GetMapping("/get")
	public ReasourceMaster findReasourceMasterById(@RequestBody ReasourceMaster ReasourceMaster) {

		return ReasourceMasterService.findReasourceMasterById(ReasourceMaster.getReasourceId());
		// return State;
	}

	@PutMapping("/update")
	public String updateReasourceMaster(@RequestBody ReasourceMaster ReasourceMaster) {
		ReasourceMasterService.updateReasourceMaster(ReasourceMaster);
		return "Record Updated.....";
	}

	@DeleteMapping("/delete")
	public String deleteReasourceMasterById(@RequestBody ReasourceMaster ReasourceMaster) {
		int id = ReasourceMaster.getReasourceId();
		if (id > 0) {
			ReasourceMasterService.deleteReasourceMasterById(id);
			return "deleted..." + id;
		}
		return "Wrong Id" + id;
	}
}

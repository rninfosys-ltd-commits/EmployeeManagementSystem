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

import com.schoolapp.entity.FeesStructure;
import com.schoolapp.service.FeesStructureService;

@RestController
@RequestMapping("/feesStructure")

public class FeesStructureController {
	@Autowired
	FeesStructureService FeesStructureService;

	@PostMapping("/save")
	public String saveFeesStructure(@RequestBody List<FeesStructure> FeesStructure)
			throws ClassNotFoundException, SQLException {
		FeesStructureService.saveFeesStructure(FeesStructure);
		return "Record save successfully";
	}

	@PostMapping("/getAll")
	public String getAllFeesStructure(@RequestBody FeesStructure FeesStructure)
			throws ClassNotFoundException, SQLException {
		return FeesStructureService.getAllFeesStructure(FeesStructure);
	}

	@GetMapping("/get")
	public FeesStructure findFeesStructureById(@RequestBody FeesStructure FeesStructure) {

		return FeesStructureService.findFeesStructureById(FeesStructure.getFeesStructureId());
		// return State;
	}

	@PutMapping("/update")
	public String updateFeesStructure(@RequestBody List<FeesStructure> FeesStructure)
			throws ClassNotFoundException, SQLException {
		FeesStructureService.updateFeesStructure(FeesStructure);
		return "Record Updated.....";
	}

	@DeleteMapping("/delete")
	public String deleteFeesStructureById(@RequestBody FeesStructure FeesStructure) {
		int id = FeesStructure.getFeesStructureId();
		if (id > 0) {
			FeesStructureService.deleteFeesStructureById(id);
			return "deleted..." + id;
		}

		return "Wrong Id" + id;
	}
}

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

import com.schoolapp.entity.Unit;
import com.schoolapp.service.UnitService;

@RestController
@RequestMapping("/unit")

public class UnitController {

	@Autowired
	UnitService unitService;

	@PostMapping("/save")
	public String saveUnit(@RequestBody Unit Unit) throws ClassNotFoundException, SQLException {
		unitService.saveUnit(Unit);
		return "Record save successfully";
	}

	@GetMapping("/getAll")
	public List<Unit> getAllUnit() {
		return unitService.getAllUnit();
	}

	@GetMapping("/get")
	public Unit findUnitById(@RequestBody Unit Unit) {
		return unitService.findUnitById(Unit.getUnitId());
		// return State;
	}

	@PostMapping("/getUnitSite")
	public String getUnitSite(@RequestBody Unit unit) throws Exception {
		return unitService.getUnitSite(unit);
	}

	@PostMapping("/getUnitWing")
	public String getUnitWing(@RequestBody Unit unit) throws Exception {
		return unitService.getUnitWing(unit);
	}

	@PostMapping("/getUnitFloor")
	public String getUnitFloor(@RequestBody Unit unit) throws Exception {
		return unitService.getUnitFloor(unit);
	}

	@PutMapping("/update")
	public String updateUnit(@RequestBody Unit Unit) throws ClassNotFoundException, SQLException {
		unitService.updateUnit(Unit);
		return "Record Updated.....";
	}

	@PutMapping("/updateDeleteUnit")
	public String updateDeleteUnit(@RequestBody Unit unit) throws ClassNotFoundException, SQLException {
		unitService.updateDeleteUnit(unit);
		return "Record Updated.....";
	}

	@DeleteMapping("/delete")
	public String deleteUnitById(@RequestBody Unit Unit) {
		int id = Unit.getUnitId();
		;
		if (id > 0) {
			unitService.deleteUnitByID(id);
			return "deleted....=> " + id;
		}

		return "Wrong ID" + id;
	}
}

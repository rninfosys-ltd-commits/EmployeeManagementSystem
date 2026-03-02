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

import com.schoolapp.entity.StdAnnualFees;
import com.schoolapp.service.StdAnnualFeesService;

@RestController
@RequestMapping("/stdAnnualFees")

public class StdAnnualFeesController {
	@Autowired
	StdAnnualFeesService StdAnnualFeesService;

	@PostMapping("/save")
	public String saveStdAnnualFees(@RequestBody StdAnnualFees StdAnnualFees)
			throws ClassNotFoundException, SQLException {
		StdAnnualFeesService.saveStdAnnualFees(StdAnnualFees);
		return "Record save successfully";
	}

	@PostMapping("/getAll")
	public String getAllStdAnnualFees(@RequestBody StdAnnualFees StdAnnualFees)
			throws ClassNotFoundException, SQLException {
		return StdAnnualFeesService.getAllStdAnnualFees(StdAnnualFees);
	}

	@PostMapping("/getYearWiseStdOutstanding")
	public String getYearWiseStdOutstanding(@RequestBody StdAnnualFees StdAnnualFees)
			throws ClassNotFoundException, SQLException {
		return StdAnnualFeesService.getYearWiseStdOutstanding(StdAnnualFees);
	}

	@GetMapping("/get")
	public StdAnnualFees findStdAnnualFeesById(@RequestBody StdAnnualFees StdAnnualFees) {

		return StdAnnualFeesService.findStdAnnualFeesById(StdAnnualFees.getStdAnnualFeesId());
		// return State;
	}

	@PutMapping("/update")
	public String updateStdAnnualFees(@RequestBody StdAnnualFees StdAnnualFees) {
		StdAnnualFeesService.updateStdAnnualFees(StdAnnualFees);
		return "Record Updated.....";
	}

	@DeleteMapping("/delete")
	public String deleteStdAnnualFeesById(@RequestBody StdAnnualFees StdAnnualFees) {
		int id = StdAnnualFees.getStdAnnualFeesId();
		if (id > 0) {
			StdAnnualFeesService.deleteStdAnnualFeesById(id);
			return "deleted..." + id;
		}

		return "Wrong Id" + id;
	}
}

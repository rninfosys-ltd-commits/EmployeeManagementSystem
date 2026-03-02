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

import com.schoolapp.entity.Wing;
import com.schoolapp.service.WingService;

@RestController
@RequestMapping("/wing")

public class WingController {

	@Autowired
	WingService wingService;

	@PostMapping("/save")
	public String saveWing(@RequestBody Wing wing) throws ClassNotFoundException, SQLException {
		wingService.saveWing(wing);
		return "Record save successfully";
	}

	@GetMapping("/getAll")
	public List<Wing> getAllWing() {
		return wingService.getAllWing();
	}

	@GetMapping("/get")
	public Wing findWingById(@RequestBody Wing wing) {
		return wingService.findWingById(wing.getWingId());
		// return State;
	}

	@PutMapping("/update")
	public String updateWing(@RequestBody Wing wing) throws ClassNotFoundException, SQLException {
		wingService.updateWing(wing);
		return "Record Updated.....";
	}

	@PutMapping("/updateDeleteWing")
	public String updateDeleteWing(@RequestBody Wing wing) throws ClassNotFoundException, SQLException {
		wingService.updateDeleteWing(wing);
		return "Record Updated.....";
	}

	@DeleteMapping("/delete")
	public String deleteWingById(@RequestBody Wing wing) {
		int id = wing.getWingId();

		if (id > 0) {
			wingService.deleteWingById(id);
			return "deleted....." + id;
		}

		return "Wrong ID" + id;
	}

}

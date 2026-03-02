package com.schoolapp.controller;

import java.sql.SQLException;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.schoolapp.entity.AttendanceDetailes;
import com.schoolapp.service.AttendanceDetailesService;

@RestController
@RequestMapping("/attendanceDetailes")

public class AttendanceDetailesController {
	@Autowired
	AttendanceDetailesService AttendanceDetailesService;

	@PostMapping("/save")
	public String saveWorkOrder(@RequestBody ArrayList<AttendanceDetailes> AttendanceDetailes)
			throws ClassNotFoundException, SQLException {

		AttendanceDetailesService.saveAttendanceDetailes(AttendanceDetailes);
		return "Record save successfully";
	}

	@PostMapping("/getAll")
	public String getAllAttendanceDetailes(@RequestBody AttendanceDetailes AttendanceDetailes) throws Exception {
		return AttendanceDetailesService.getAllAttendanceDetailes(AttendanceDetailes);
	}

	@GetMapping("/get")
	public AttendanceDetailes findAttendanceDetailesById(@RequestBody AttendanceDetailes AttendanceDetailes) {

		return AttendanceDetailesService.findAttendanceDetailesById(AttendanceDetailes.getAttendanceId());

	}

	@PutMapping("/updateDeleteAttendanceDetailes")
	public String updateDeleteAttendanceDetailes(@RequestBody AttendanceDetailes AttendanceDetailes)
			throws ClassNotFoundException, SQLException {
		AttendanceDetailesService.updateDeleteAttendanceDetailes(AttendanceDetailes);
		return "Record Updated.....";
	}

	@PutMapping("/update")
	public String updateAttendanceDetailes(@RequestBody ArrayList<AttendanceDetailes> AttendanceDetailes)
			throws ClassNotFoundException, SQLException {
		AttendanceDetailesService.updateAttendanceDetailes(AttendanceDetailes);
		return "Record Updated.....";
	}

	// http://192.168.43.146:8080/leadAccounts/getAll
	@DeleteMapping("/delete")
	public String deleteAttendanceDetailesById(@RequestBody AttendanceDetailes AttendanceDetailes) {
		int id = AttendanceDetailes.getAttendanceId();

		if (id > 0) {
			AttendanceDetailesService.deleteAttendanceDetailesById(id);
			return "deleted....." + id;
		}

		return "Wrong ID" + id;
	}
}

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

import com.schoolapp.entity.StudentInfo;
import com.schoolapp.service.StudentInfoService;

@RestController
@RequestMapping("/studentInfo")

public class StudentInfoController {
	@Autowired
	StudentInfoService studentInfoService;

	@PostMapping("/save")
	public String saveStudentInfo(@RequestBody StudentInfo studentInfo) throws ClassNotFoundException, SQLException {
		studentInfoService.saveStudentInfo(studentInfo);
		return "Record save successfully";

	}

	@PostMapping("/getAll")
	public String getAllStudentInfo(@RequestBody StudentInfo studentInfo) throws ClassNotFoundException, SQLException {
		return studentInfoService.getAllStudentInfo(studentInfo);
	}

	@GetMapping("/get")
	public StudentInfo findStudentInfoById(@PathVariable int studentInfoID) {
		return studentInfoService.findStudentInfoById(studentInfoID);
	}

	@PutMapping("/update")
	public String updateStudentInfo(@RequestBody StudentInfo studentInfo) throws ClassNotFoundException, SQLException {
		studentInfoService.updateStudentInfo(studentInfo);
		return "Record Updated.......";

	}

	@DeleteMapping("/delete")
	public String deleteStudentInfoById(@RequestBody StudentInfo studentInfo) {
		studentInfoService.deleteStudentInfoById(studentInfo.getStudentInfoId());
		return "deleted............";
	}
}

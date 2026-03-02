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

import com.schoolapp.entity.ClassMaster;
import com.schoolapp.service.ClassMasterService;

@RestController
@RequestMapping("/classMaster")

public class ClassMasterController {
	@Autowired
	ClassMasterService ClassMasterService;

	@PostMapping("/save")
	public String saveClassMaster(@RequestBody ClassMaster ClassMaster) throws ClassNotFoundException, SQLException {
		ClassMasterService.saveClassMaster(ClassMaster);
		return "Record save successfully";
	}

	@PostMapping("/getAll")
	public String getAllClassMaster(@RequestBody ClassMaster ClassMaster) throws ClassNotFoundException, SQLException {
		return ClassMasterService.getAllClassMaster(ClassMaster);
	}

	@GetMapping("/get")
	public ClassMaster findClassMasterById(@RequestBody ClassMaster ClassMaster) {

		return ClassMasterService.findClassMasterById(ClassMaster.getClassId());
		// return State;
	}

	@PutMapping("/update")
	public String updateClassMaster(@RequestBody ClassMaster ClassMaster) {
		ClassMasterService.updateClassMaster(ClassMaster);
		return "Record Updated.....";
	}

	@DeleteMapping("/delete")
	public String deleteClassMasterById(@RequestBody ClassMaster ClassMaster) {
		int id = ClassMaster.getClassId();
		if (id > 0) {
			ClassMasterService.deleteClassMasterById(id);
			return "deleted..." + id;
		}

		return "Wrong Id" + id;
	}
}

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

import com.schoolapp.entity.WorkCompletion;
import com.schoolapp.service.WorkCompletionService;

@RestController
@RequestMapping("/workCompletion")

public class WorkCompletionController {
	@Autowired
	WorkCompletionService workCompletionService;

	@PostMapping("/save")
	public String saveWorkCompletion(@RequestBody WorkCompletion workCompletion)
			throws ClassNotFoundException, SQLException {
		workCompletionService.saveWorkCompletion(workCompletion);
		return "Record save successfully";
	}

	@GetMapping("/getAll")
	public String getAllWorkCompletion() throws Exception {
		return workCompletionService.getAllWorkCompletion();
	}

	//
	@GetMapping("/get")
	public WorkCompletion findWorkCompletionById(@RequestBody WorkCompletion workCompletion) {

		return workCompletionService.findWorkCompletionById(workCompletion.getWorkCompletionId());
	}

	@PutMapping("/updateDeleteWorkCompletion")
	public String updateDeleteWorkCompletion(@RequestBody WorkCompletion workCompletion)
			throws ClassNotFoundException, SQLException {
		workCompletionService.updateDeleteWorkCompletion(workCompletion);
		return "Record Updated.....";
	}

	@PutMapping("/update")
	public String updateWorkCompletion(@RequestBody WorkCompletion workCompletion)
			throws ClassNotFoundException, SQLException {
		workCompletionService.updateWorkCompletion(workCompletion);
		return "Record Updated.....";
	}

	// http://192.168.43.146:8080/leadAccounts/getAll
	@DeleteMapping("/delete")
	public String deleteWorkCompletionById(@RequestBody WorkCompletion workCompletion) {
		int id = workCompletion.getWorkCompletionId();

		if (id > 0) {
			workCompletionService.deleteWorkCompletionById(id);
			return "deleted....." + id;
		}

		return "Wrong ID" + id;
	}
}

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

import com.schoolapp.entity.Branch;
import com.schoolapp.service.BranchService;

@RestController
@RequestMapping("/branch")

public class BranchController {

	@Autowired
	BranchService branchService;

	@PostMapping("/save")
	public String saveBranch(@RequestBody Branch branch) throws ClassNotFoundException, SQLException {
		branchService.saveBranch(branch);
		return "Record save successfully";
	}

	@GetMapping("/getAll")
	public List<Branch> getAllBranch() {
		return branchService.getAllBranch();
	}

	@GetMapping("/get")
	public Branch findBranchById(@RequestBody Branch branch) {

		return branchService.findBranchById(branch.getBranchId());
		// return State;
	}

	@PutMapping("/updateDeleteBranch")
	public String updateDeleteBranch(@RequestBody Branch branch) throws ClassNotFoundException, SQLException {
		branchService.updateDeleteBranch(branch);
		return "Record Updated.....";
	}

	@PutMapping("/update")
	public String updateBranch(@RequestBody Branch branch) throws ClassNotFoundException, SQLException {
		branchService.updateBranch(branch);
		return "Record Updated.....";
	}

	@DeleteMapping("/delete")
	public String deleteBranchById(@RequestBody Branch branch) {
		int id = branch.getBranchId();

		if (id > 0) {
			branchService.deleteBranchById(id);
			return "deleted....." + id;
		}

		return "Wrong ID" + id;
	}

}

package com.schoolapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.schoolapp.entity.SubCast;
import com.schoolapp.service.SubCastService;

@RestController
@RequestMapping("/subCast")

public class SubCastController {
	@Autowired
	SubCastService SubCastService;

	@PostMapping("/save")
	public String saveSubCast(@RequestBody SubCast SubCast) {
		SubCastService.saveSubCast(SubCast);
		return "Record save successfully";
	}

	@GetMapping("/getAll")
	public List<SubCast> getAllSubCast() {
		return SubCastService.getAllSubCast();
	}

	@GetMapping("/get")
	public SubCast findSubCastById(@RequestBody SubCast SubCast) {

		return SubCastService.findSubCastById(SubCast.getSubCastId());
		// return State;
	}

	@PutMapping("/update")
	public String updateSubCast(@RequestBody SubCast SubCast) {
		SubCastService.updateSubCast(SubCast);
		return "Record Updated.....";
	}

	@DeleteMapping("/delete")
	public String deleteSubCastById(@RequestBody SubCast SubCast) {
		int id = SubCast.getSubCastId();
		if (id > 0) {
			SubCastService.deleteSubCastById(id);
			return "deleted..." + id;
		}

		return "Wrong Id" + id;
	}
}

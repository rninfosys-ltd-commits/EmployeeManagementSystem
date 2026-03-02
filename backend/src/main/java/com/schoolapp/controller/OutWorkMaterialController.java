package com.schoolapp.controller;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.schoolapp.entity.OutWorkMaterial;
import com.schoolapp.service.OutWorkMaterialService;

@RestController
@RequestMapping("/OutWorkMaterial")

public class OutWorkMaterialController {

	@Autowired
	OutWorkMaterialService OutWorkMaterialService;

	// @PostMapping("/save")
	// public String saveOutWorkMaterial(@RequestBody OutWorkMaterial
	// OutWorkMaterial) throws ClassNotFoundException, SQLException {
	// OutWorkMaterialService.saveOutWorkMaterial(OutWorkMaterial);
	// return "Record save successfully";
	// }

	@PostMapping("/save")
	public List<OutWorkMaterial> saveOutWorkMaterial(@RequestBody List<OutWorkMaterial> OutWorkMaterial)
			throws ClassNotFoundException, SQLException {

		return OutWorkMaterialService.saveOutWorkMaterial(OutWorkMaterial);
	}

	@PostMapping("/getAll")
	public String getAllOutWorkMaterial(@RequestBody OutWorkMaterial OutWorkMaterial) throws Exception {

		return OutWorkMaterialService.getAllOutWorkMaterial(OutWorkMaterial);
	}

	@PostMapping("/getAllStudentClassWise")
	public String getAllStudentClassWise(@RequestBody OutWorkMaterial OutWorkMaterial) throws Exception {
		return OutWorkMaterialService.getAllStudentClassWise(OutWorkMaterial);
	}

	@GetMapping("/get")
	public OutWorkMaterial findOutWorkMaterialById(@PathVariable int OutWorkMaterialID) {
		return OutWorkMaterialService.findOutWorkMaterialById(OutWorkMaterialID);
	}

	@PutMapping("/update")
	public OutWorkMaterial updateDeleteOutWorkMaterial(@RequestBody OutWorkMaterial OutWorkMaterial)
			throws ClassNotFoundException, SQLException {
		return OutWorkMaterialService.updateDeleteOutWorkMaterial(OutWorkMaterial);
	}

	@DeleteMapping("/delete")
	public String deleteOutWorkMaterialById(@RequestBody OutWorkMaterial OutWorkMaterial) {
		OutWorkMaterialService.deleteOutWorkMaterialById(OutWorkMaterial.getOutWorkMaterialId());
		return "deleted............";
	}
}

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

import com.schoolapp.entity.ProductionRecipeMaster;
import com.schoolapp.service.ProductionRecipeMasterService;

@RestController
@RequestMapping("/ProductionRecipeMaster")

public class ProductionRecipeMasterController {
	@Autowired
	ProductionRecipeMasterService ProductionRecipeMasterService;

	@PostMapping("/save")
	public String saveProductionRecipeMaster(@RequestBody ProductionRecipeMaster ProductionRecipeMaster)
			throws ClassNotFoundException, SQLException {
		ProductionRecipeMasterService.saveProductionRecipeMaster(ProductionRecipeMaster);
		return "Record save successfully";
	}

	@PostMapping("/getAll")
	public String getAllProductionRecipeMaster(@RequestBody ProductionRecipeMaster ProductionRecipeMaster)
			throws Exception {

		return ProductionRecipeMasterService.getAllProductionRecipeMaster(ProductionRecipeMaster);
	}

	@PostMapping("/getAllStudentClassWise")
	public String getAllStudentClassWise(@RequestBody ProductionRecipeMaster ProductionRecipeMaster) throws Exception {
		return ProductionRecipeMasterService.getAllStudentClassWise(ProductionRecipeMaster);
	}

	@GetMapping("/get")
	public ProductionRecipeMaster findProductionRecipeMasterById(@PathVariable int ProductionRecipeMasterID) {
		return ProductionRecipeMasterService.findProductionRecipeMasterById(ProductionRecipeMasterID);
	}

	@PutMapping("/update")
	public ProductionRecipeMaster updateDeleteProductionRecipeMaster(
			@RequestBody ProductionRecipeMaster ProductionRecipeMaster) throws ClassNotFoundException, SQLException {
		return ProductionRecipeMasterService.updateDeleteProductionRecipeMaster(ProductionRecipeMaster);
	}

	@DeleteMapping("/delete")
	public String deleteProductionRecipeMasterById(@RequestBody ProductionRecipeMaster ProductionRecipeMaster) {
		ProductionRecipeMasterService
				.deleteProductionRecipeMasterById(ProductionRecipeMaster.getProductionRecipeMasterId());
		return "deleted............";
	}
}

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

import com.schoolapp.entity.ProductionRecipe;
import com.schoolapp.service.ProductionRecipeService;

@RestController
@RequestMapping("/productionRecipe")

public class ProductionRecipeController {
	@Autowired
	ProductionRecipeService ProductionRecipeService;

	@PostMapping("/save")
	public String saveProductionRecipe(@RequestBody ProductionRecipe ProductionRecipe)
			throws ClassNotFoundException, SQLException {
		ProductionRecipeService.saveProductionRecipe(ProductionRecipe);
		return "Record save successfully";
	}

	@PostMapping("/getAll")
	public String getAllProductionRecipe(@RequestBody ProductionRecipe ProductionRecipe)
			throws Exception {

		return ProductionRecipeService.getAllProductionRecipe(ProductionRecipe);
	}

	@PostMapping("/getAllStudentClassWise")
	public String getAllStudentClassWise(@RequestBody ProductionRecipe ProductionRecipe)
			throws Exception {
		return ProductionRecipeService.getAllStudentClassWise(ProductionRecipe);
	}

	@GetMapping("/get")
	public ProductionRecipe findProductionRecipeById(@PathVariable int ProductionRecipeID) {
		return ProductionRecipeService.findProductionRecipeById(ProductionRecipeID);
	}

	@PutMapping("/update")
	public ProductionRecipe updateDeleteProductionRecipe(@RequestBody ProductionRecipe ProductionRecipe)
			throws ClassNotFoundException, SQLException {
		return ProductionRecipeService.updateDeleteProductionRecipe(ProductionRecipe);
	}

	@DeleteMapping("/delete")
	public String deleteProductionRecipeById(@RequestBody ProductionRecipe ProductionRecipe) {
		ProductionRecipeService.deleteProductionRecipeById(ProductionRecipe.getProductionRecipeId());
		return "deleted............";
	}
}

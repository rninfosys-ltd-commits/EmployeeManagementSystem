package com.schoolapp.dao;

import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.schoolapp.entity.ProductionRecipe;
import com.schoolapp.repository.ProductionRecipeRepo;

@Component
public class ProductionRecipeDao {
	@Autowired
	ProductionRecipeRepo ProductionRecipeRepo;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public ProductionRecipe saveProductionRecipe(ProductionRecipe ProductionRecipe) {
		System.out.println("Data inserted successfully...");
		return ProductionRecipeRepo.save(ProductionRecipe);
	}

	public String getAllProductionRecipe(ProductionRecipe ProductionRecipe) {
		int orgId = ProductionRecipe.getOrgId();
		String sql = "SELECT * FROM production_recipe where org_id = ?";
		List<Map<String, Object>> result = jdbcTemplate.queryForList(sql, orgId);
		return new JSONArray(result).toString();
	}

	public ProductionRecipe updateDeleteProductionRecipe(ProductionRecipe ProductionRecipe) {
		return ProductionRecipeRepo.save(ProductionRecipe);
	}

	public ProductionRecipe findProductionRecipeById(int ProductionRecipeId) {
		return (ProductionRecipe) ProductionRecipeRepo.findById(ProductionRecipeId).get();
	}

	public String deleteProductionRecipeById(int ProductionRecipeId) {
		ProductionRecipeRepo.deleteById(ProductionRecipeId);
		return "deleted";
	}
}

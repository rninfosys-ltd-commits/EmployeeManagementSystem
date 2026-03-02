package com.schoolapp.dao;

import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.schoolapp.entity.ProductionRecipeMaster;
import com.schoolapp.repository.ProductionRecipeMasterRepo;

@Component
public class ProductionRecipeMasterDao {
	@Autowired
	ProductionRecipeMasterRepo ProductionRecipeMasterRepo;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public ProductionRecipeMaster saveProductionRecipeMaster(ProductionRecipeMaster ProductionRecipeMaster) {
		System.out.println("Data inserted successfully...");
		return ProductionRecipeMasterRepo.save(ProductionRecipeMaster);
	}

	public String getAllProductionRecipeMaster(ProductionRecipeMaster ProductionRecipeMaster) {
		int orgId = ProductionRecipeMaster.getOrgId();
		String sql = "SELECT * FROM production_recipe_master where org_id = ?";
		List<Map<String, Object>> result = jdbcTemplate.queryForList(sql, orgId);
		return new JSONArray(result).toString();
	}

	public ProductionRecipeMaster updateDeleteProductionRecipeMaster(ProductionRecipeMaster ProductionRecipeMaster) {
		return ProductionRecipeMasterRepo.save(ProductionRecipeMaster);
	}

	public ProductionRecipeMaster findProductionRecipeMasterById(int ProductionRecipeMasterId) {
		return (ProductionRecipeMaster) ProductionRecipeMasterRepo.findById(ProductionRecipeMasterId).get();
	}

	public String deleteProductionRecipeMasterById(int ProductionRecipeMasterId) {
		ProductionRecipeMasterRepo.deleteById(ProductionRecipeMasterId);
		return "deleted";
	}
}

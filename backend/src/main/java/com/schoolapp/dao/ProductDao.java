package com.schoolapp.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.schoolapp.entity.Product;
import com.schoolapp.repository.ProductRepo;

@Component
public class ProductDao {
	@Autowired
	ProductRepo productRepo;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public Product saveProduct(Product product) {
		return productRepo.save(product);
	}

	public String updateProduct(Product product) {
		Integer productId = product.getProductId();
		String name = product.getName();
		String unit = product.getUnit();
		int userId = product.getUserId();
		float quantity = product.getQuantity();
		float rate = product.getRate();
		float d1 = product.getD1();
		float d2 = product.getD2();
		String itmType = product.getItmType();
		String shrtnm = product.getShrtnm();
		String productdiscription = product.getProductdiscription();
		float cgst = product.getCgst();
		float sgst = product.getSgst();
		float igst = product.getIgst();
		float purchaseRate = product.getPurchaseRate();
		String hsncode = product.getHsncode();
		int category = product.getCategory();
		int categoryID = product.getCategoryID();
		int brandId = product.getBrandId();
		float wrates = product.getWrates();
		int brId = product.getBrId();
		int orgId = product.getOrgId();
		int custId = product.getCustId();
		int isActive = product.getIsActive();
		java.sql.Date createdDate = product.getCreatedDate();
		int updatedBy = product.getUserId();
		java.sql.Date updatedDate = product.getUpdatedDate();

		String sql = "UPDATE product SET br_id = ?, brand_id = ?, category = ?, categoryid = ?, cgst = ?, cust_id = ?, "
				+ "d1 = ?, d2 = ?, hsncode = ?, igst = ?, itm_type = ?, name = ?, org_id = ?, productdiscription = ?, "
				+ "purchase_rate = ?, quantity = ?, rate = ?, sgst = ?, shrtnm = ?, unit = ?, user_id = ?, wrates = ?, "
				+ "created_date = ?, updated_by = ?, updated_date = ?, is_active = ? WHERE product_id = ? AND org_id = ?";

		jdbcTemplate.update(sql, brId, brandId, category, categoryID, cgst, custId, d1, d2, hsncode, igst, itmType,
				name,
				orgId, productdiscription, purchaseRate, quantity, rate, sgst, shrtnm, unit, userId, wrates,
				createdDate,
				updatedBy, updatedDate, isActive, productId, orgId);

		return "Record updated..!";
	}

	public Product updateDeleteProduct(Product product) {
		Integer productId = product.getProductId();
		int updatedBy = product.getUpdatedBy();
		java.sql.Date updatedDate = product.getUpdatedDate();
		int orgId = product.getOrgId();
		int isActive = product.getIsActive();

		String sql = "UPDATE product SET is_active = ?, updated_by = ?, updated_date = ? "
				+ "WHERE product_id = ? AND org_id = ?";

		jdbcTemplate.update(sql, isActive, updatedBy, updatedDate, productId, orgId);

		return productRepo.save(product);
	}

	public List<Product> getAllProduct() {
		return productRepo.findAll();
	}

	public Product findProductById(int Product) {
		return productRepo.findById(Product).get();
	}

	public String deleteProductById(int Product) {
		productRepo.deleteById(Product);
		return "deleted";
	}

}

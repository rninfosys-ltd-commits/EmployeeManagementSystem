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

import com.schoolapp.entity.Product;
import com.schoolapp.service.ProductService;

@RestController
@RequestMapping("/product")

public class ProductController {

	@Autowired
	ProductService productService;

	@PostMapping("/save")
	public String saveProduct(@RequestBody Product product) throws ClassNotFoundException, SQLException {
		productService.saveProduct(product);
		return "Record save successfully";
	}

	@GetMapping("/getAll")
	public List<Product> getAllProduct() {
		return productService.getAllProduct();
	}

	@GetMapping("/get")
	public Product findProductById(@RequestBody Product product) {

		return productService.findProductById(product.getProductId());
		// return State;
	}

	@PutMapping("/updateDeleteProduct")
	public String updateDeleteProduct(@RequestBody Product product)
			throws ClassNotFoundException, SQLException {
		productService.updateDeleteProduct(product);
		return "Record Updated.....";
	}

	@PutMapping("/update")
	public String updateProduct(@RequestBody Product product) throws ClassNotFoundException, SQLException {
		productService.updateProduct(product);
		return "Record Updated.....";
	}

	@DeleteMapping("/delete")
	public String deleteProductById(@RequestBody Product product) {
		int id = product.getProductId();

		if (id > 0) {
			productService.deleteProductById(id);
			return "deleted....." + id;
		}

		return "Wrong ID" + id;
	}

}

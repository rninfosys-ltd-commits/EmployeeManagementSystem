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

import com.schoolapp.entity.Category;
import com.schoolapp.service.CategoryService;

@RestController
@RequestMapping("/category")

public class CategoryController {
	@Autowired
	CategoryService CategoryService;

	@PostMapping("/save")
	public String saveCategory(@RequestBody Category Category) {
		CategoryService.saveCategory(Category);
		return "Record save successfully";
	}

	@GetMapping("/getAll")
	public List<Category> getAllCategory() {
		return CategoryService.getAllCategory();
	}

	@GetMapping("/get")
	public Category findCategoryById(@RequestBody Category Category) {

		return CategoryService.findCategoryById(Category.getCategoryId());

	}

	@PutMapping("/update")
	public String updateCategory(@RequestBody Category Category) {
		CategoryService.updateCategory(Category);
		return "Record Updated.....";
	}

	@DeleteMapping("/delete")
	public String deleteCategoryById(@RequestBody Category Category) {
		int id = Category.getCategoryId();
		if (id > 0) {
			CategoryService.deleteCategoryById(id);
			return "deleted..." + id;
		}
		return "Wrong Id" + id;
	}
}

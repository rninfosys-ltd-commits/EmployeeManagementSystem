package com.schoolapp.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.schoolapp.entity.Category;

@Component
public class CategoryDao {
	@Autowired
	com.schoolapp.repository.CategoryRepo CategoryRepo;

	public Category saveCategory(Category Category) {
		return CategoryRepo.save(Category);
	}

	public List<Category> getAllCategory() {
		return CategoryRepo.findAll();
	}

	public Category findCategoryById(int CategoryId) {
		return CategoryRepo.findById(CategoryId).get();
	}

	public String deleteCategoryById(int CategoryId) {
		CategoryRepo.deleteById(CategoryId);
		return "deleted";
	}
}

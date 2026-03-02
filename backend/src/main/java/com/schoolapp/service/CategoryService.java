package com.schoolapp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.schoolapp.entity.Category;

@Service
public class CategoryService {
	@Autowired

	com.schoolapp.dao.CategoryDao CategoryDao;

	public Category saveCategory(Category Category) {
		return CategoryDao.saveCategory(Category);
	}

	public List<Category> getAllCategory() {
		return CategoryDao.getAllCategory();
	}

	public Category findCategoryById(int CategoryId) {
		return CategoryDao.findCategoryById(CategoryId);
	}

	public Category updateCategory(Category CategoryId) {
		Category Category = CategoryDao.findCategoryById(CategoryId.getCategoryId());
		Category.setCategoryId(CategoryId.getCategoryId());
		Category.setCategoryName(CategoryId.getCategoryName());
		return CategoryDao.saveCategory(Category);
	}

	public String deleteCategoryById(int CategoryId) {
		CategoryDao.deleteCategoryById(CategoryId);
		return "deleted";
	}
}

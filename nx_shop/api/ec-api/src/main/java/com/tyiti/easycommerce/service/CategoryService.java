package com.tyiti.easycommerce.service;

import java.util.List;

import com.tyiti.easycommerce.entity.Category;

public interface CategoryService {
	Category getById(Integer id);
	Category addCategory(Category category);
	List<Category> getChildren(List<Integer> ids);
	List<Category> getAll();
	List<Category> getRootCategories();
}

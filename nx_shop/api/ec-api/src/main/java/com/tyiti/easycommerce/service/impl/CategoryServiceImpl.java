package com.tyiti.easycommerce.service.impl;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tyiti.easycommerce.entity.Category;
import com.tyiti.easycommerce.repository.CategoryDao;
import com.tyiti.easycommerce.service.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService {
	@Autowired
	CategoryDao categoryDao;

	@Override
	public Category addCategory(Category category) {
		int affectedRows = categoryDao.add(category);
		if (affectedRows < 1) {
			return null;
		}
		return categoryDao.getById(category.getId());
	}

	@Override
	public Category getById(Integer id) {
		return categoryDao.getById(id);
	}

	@Override
	public List<Category> getChildren(List<Integer> ids) {
		return categoryDao.getChildren(ids);
	}
	
	public void findDescendant(List<Category> categories) {
		if (categories == null || categories.size() == 0) {
			return;
		}

		List<Integer> ids = new ArrayList<Integer>(categories.size());
		for (Category category : categories) {
			ids.add(category.getId());
		}
		List<Category> children = getChildren(ids);
		for (Category child : children) {
			for (Category category : categories) {
				if (category.getId().equals(child.getParentId())) {
					List<Category> childrenOfThis = category.getChildren();
					if (childrenOfThis == null) {
						childrenOfThis = new LinkedList<Category>();
						category.setChildren(childrenOfThis);
					}
					childrenOfThis.add(child);
					break;
				}
			}
		}
		findDescendant(children);
	}

	@Override
	public List<Category> getRootCategories() {
		return categoryDao.getRootCategories();
	}

	@Override
	public List<Category> getAll() {
		List<Category> rootCategories = this.getRootCategories();
		findDescendant(rootCategories);
		return rootCategories;
	}

}

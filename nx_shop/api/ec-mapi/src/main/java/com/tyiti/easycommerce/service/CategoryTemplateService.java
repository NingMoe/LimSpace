package com.tyiti.easycommerce.service;

import java.util.List;

import com.tyiti.easycommerce.entity.CategoryTemplate;

public interface CategoryTemplateService {


	void updateTemplate(CategoryTemplate categoryTemplate);

	CategoryTemplate selectTemplate(int id);

	void insertTemplate(List<CategoryTemplate> categoryTemplateList);

	void deleteTemplate(int categoryId);

	List<CategoryTemplate> getTemplateList(int parenId);

	void deleteByCategoryType(Integer categoryId, int type);

}

package com.tyiti.easycommerce.repository;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.tyiti.easycommerce.entity.Category;

public interface CategoryDao {
    int add(Category category);
    
    Category getById(@Param("id")Integer id);
    
    List<Category> getChildren(@Param("ids")List<Integer> ids);
    
    List<Category> getRootCategories();
}

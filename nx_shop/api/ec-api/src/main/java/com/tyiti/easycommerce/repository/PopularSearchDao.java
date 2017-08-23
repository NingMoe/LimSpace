package com.tyiti.easycommerce.repository;

import java.util.List;

import com.tyiti.easycommerce.entity.PopularSearch;


public interface PopularSearchDao {
	
	List<PopularSearch> getAll();

}
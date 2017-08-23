package com.tyiti.easycommerce.service;

import java.util.List;
import java.util.Map;

import com.tyiti.easycommerce.entity.PopularSearch;

public interface PopularSearchService {

	List<Map<String, Object>> getAll();
	
	PopularSearch getById(Integer id);
	
	Integer add(PopularSearch popularSearch);
	
	Integer delete(Integer id);
	
	Integer updateStatus(Integer id);
	
	Integer updateRank(PopularSearch popularSearch);
}
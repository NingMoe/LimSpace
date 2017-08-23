package com.tyiti.easycommerce.repository;

import java.util.List;
import java.util.Map;

import com.tyiti.easycommerce.entity.PopularSearch;


public interface PopularSearchDao {
	
	List<Map<String, Object>> getAll();
	
	PopularSearch getById(Integer id);
	
	Integer add(PopularSearch popularSearch);
	
	Integer delete(Integer id);
	
	Integer updateStatus(Integer id);
	
	Integer rankBottonUp(PopularSearch popularSearch);
	
	Integer rankTopDown(PopularSearch popularSearch);
	
	Integer updateRank(PopularSearch popularSearch);
	
	Integer getRankById(Integer id);
	
	Integer getMaxRank();
}
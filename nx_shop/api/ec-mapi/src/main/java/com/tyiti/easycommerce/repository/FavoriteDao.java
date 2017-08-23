package com.tyiti.easycommerce.repository;

import java.util.List;
import java.util.Map;

public interface FavoriteDao {
	
	List<Map<String, Object>> getAll(Map<String,Object> param);
	
	long getCount(Map<String,Object> param);
}
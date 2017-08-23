package com.tyiti.easycommerce.service;

import java.util.Map;

import com.tyiti.easycommerce.base.SearchResult;

public interface FavoriteService {

	SearchResult<Map<String, Object>> getAll(Map<String,Object> param) ;

}
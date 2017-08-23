package com.tyiti.easycommerce.service;

import java.util.List;
import java.util.Map;

import com.tyiti.easycommerce.base.SearchResult;
import com.tyiti.easycommerce.entity.Sku;

public interface SkuService {

	Sku getSkuById(Integer refId);

	SearchResult<Map<String, Object>> exportSkus(Map<String,Object> param);
	
	int updateByObject(List<Sku> list);
}

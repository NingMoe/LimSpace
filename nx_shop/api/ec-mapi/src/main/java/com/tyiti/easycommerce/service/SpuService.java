package com.tyiti.easycommerce.service;

import java.util.Map;

import com.tyiti.easycommerce.base.SearchResult;
import com.tyiti.easycommerce.entity.Spu;


public interface SpuService {

	void add(Spu spu);

	Spu getById(Integer id);

	SearchResult<Map<String, Object>> getByParmas(Map<String, Object> params);

	void update(Spu spu);

	void del(int id);

	SearchResult<Map<String, Object>> search(Map<String, Object> param);

	void rank(int id, Integer rank);

}

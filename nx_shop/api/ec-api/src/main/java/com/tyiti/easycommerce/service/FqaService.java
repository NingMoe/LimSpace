package com.tyiti.easycommerce.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tyiti.easycommerce.base.SearchResult;
import com.tyiti.easycommerce.repository.FaqDao;

@Service
public class FqaService {
	@Autowired
	private FaqDao fqaDao;
	/**
	 * 分页查询
	 * @param param
	 * @return
	 */
	public SearchResult<Map<String, Object>> queryFqaByPage(
			Map<String, Object> param) {
		if(param.get("limit")!=null &&param.get("limit")!=""){
			param.put("limit", Integer.parseInt(String.valueOf(param.get("limit"))));
		}
		if(param.get("offset")!=null && param.get("offset")!=""){
			param.put("offset", Integer.parseInt(String.valueOf(param.get("offset"))));
		}
		SearchResult<Map<String,Object>> searchResult  = new SearchResult<Map<String,Object>>();
		List<Map<String, Object>> rows = fqaDao.selectFqaByPage(param);
		searchResult.setRows(rows);
		searchResult.setTotal(fqaDao.selectFqaCountByPage(param));
		return searchResult;
	}
}

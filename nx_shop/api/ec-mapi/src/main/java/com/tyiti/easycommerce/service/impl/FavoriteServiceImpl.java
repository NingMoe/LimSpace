package com.tyiti.easycommerce.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tyiti.easycommerce.base.SearchResult;
import com.tyiti.easycommerce.repository.FavoriteDao;
import com.tyiti.easycommerce.service.FavoriteService;

@Service("favoriteService")
public class FavoriteServiceImpl implements FavoriteService {
	@Autowired
	private FavoriteDao favoriteDao;

	@Override
	public SearchResult<Map<String, Object>> getAll(Map<String,Object> param) {
		if(param.get("limit")!=null &&param.get("limit")!=""){
			param.put("limit", Integer.parseInt(String.valueOf(param.get("limit"))));
		}
		if(param.get("offset")!=null && param.get("offset")!=""){
			param.put("offset", Integer.parseInt(String.valueOf(param.get("offset"))));
		}
		if(param.get("startTime")!=null && param.get("startTime")!=""){
			String startTime = param.get("startTime") + " 00:00:00";
			param.put("startTime",startTime);
		}
		if(param.get("endTime")!=null && param.get("endTime")!=""){
			String endTime = param.get("endTime") + " 23:59:59";
			param.put("endTime", endTime);
		}
		SearchResult<Map<String,Object>> searchResult = new SearchResult<Map<String,Object>>();
		searchResult.setRows(this.favoriteDao.getAll(param));
		searchResult.setTotal(this.favoriteDao.getCount(param));
		return searchResult;
	}
}

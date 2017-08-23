package com.tyiti.easycommerce.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tyiti.easycommerce.base.SearchResult;
import com.tyiti.easycommerce.entity.RushBuySku;
import com.tyiti.easycommerce.repository.RushBuySkuDao;
import com.tyiti.easycommerce.service.RushBuySkuService;
/**
 * 
 * Title:RushBuySkuServiceImpl.java
 * Description: 通用秒杀商品管理
 * @author: xulihui
 * @date: 2016年4月13日 下午5:14:00
 */
@Service
public class RushBuySkuServiceImpl implements RushBuySkuService{
    
	@Autowired
	private  RushBuySkuDao rushBuySkuDao;
	/**
	 * 商品列表
	 */
	@Override
	public SearchResult<Map<String, Object>> getRushBuySkuList(Map<String, Object> param) {
		if(param.get("limit")!=null &&param.get("limit")!=""){
			param.put("limit", Integer.parseInt(String.valueOf(param.get("limit"))));
		}
		if(param.get("offset")!=null && param.get("offset")!=""){
			param.put("offset", Integer.parseInt(String.valueOf(param.get("offset"))));
		}
		SearchResult<Map<String,Object>> searchResult  = new SearchResult<Map<String,Object>>();
		searchResult.setRows(rushBuySkuDao.selectRushBuySkuList(param));
		searchResult.setTotal(rushBuySkuDao.selectRushBuySkuCount());
		 return searchResult;
	}
	/**
	 * 添加秒杀商品
	 */
	@Override
	public int insertRushBuySku(RushBuySku rushBuySku) {
		
		return rushBuySkuDao.insertRushBuySku(rushBuySku);
	}



}

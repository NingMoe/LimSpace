package com.tyiti.easycommerce.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tyiti.easycommerce.base.SearchResult;
import com.tyiti.easycommerce.entity.RushBuy;
import com.tyiti.easycommerce.repository.RushBuyDao;
import com.tyiti.easycommerce.service.RushBuyService;
/**
 * 通用秒杀
 * Title:RushBuyServiceImpl.java
 * Description:秒杀活动
 * @author: xulihui
 * @date: 2016年4月8日 下午3:07:34
 */
@Service
public class RushBuyServiceImpl implements RushBuyService {
       
	@Autowired
	 private  RushBuyDao rushBuyDao; 
	
    /**
     * 新增秒杀
     */
	@Override
	public int insertRushBuy(RushBuy rushBuy) {
		return rushBuyDao.insertRushBuy(rushBuy);
	}
    /**
     * 删除秒杀
     */
	@Override
	public int delRushBuy(int id) {
		return rushBuyDao.delRushBuy(id);
	}
    /**
     * 更新秒杀
     */
	@Override
	public int updateRushBuy(RushBuy rushBuy) {
		
		return rushBuyDao.updateRushBuy(rushBuy);
	}
    /**
     * 查询秒杀
     */
	@Override
	public List<RushBuy> RushBuyList(Integer id) {
		// TODO Auto-generated method stub
		return rushBuyDao.selectRushBuyList(id);
	}
	/**
	 * 查询秒杀列表
	 */
	@Override
	public SearchResult<Map<String, Object>> getRushBuyList(Map<String, Object> param) {
		
		if(param.get("limit")!=null &&param.get("limit")!=""){
			param.put("limit", Integer.parseInt(String.valueOf(param.get("limit"))));
		}
		if(param.get("offset")!=null && param.get("offset")!=""){
			param.put("offset", Integer.parseInt(String.valueOf(param.get("offset"))));
		}
		SearchResult<Map<String,Object>> searchResult  = new SearchResult<Map<String,Object>>();
		searchResult.setRows(rushBuyDao.selectRushBuyLists(param));
		searchResult.setTotal(rushBuyDao.selectRushBuyCount());
		 return searchResult;
	}  
}

package com.tyiti.easycommerce.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tyiti.easycommerce.base.SearchResult;
import com.tyiti.easycommerce.entity.Sku;
import com.tyiti.easycommerce.repository.SkuDao;
import com.tyiti.easycommerce.service.SkuService;

@Service("skuService")
public class SkuServiceImpl implements SkuService {

	@Autowired
	private SkuDao skuDao ; 
	@Override
	public Sku getSkuById(Integer id) {
		// TODO Auto-generated method stub
		return skuDao.selectByPrimaryKey(id);
	}
 
	public SearchResult<Map<String, Object>> exportSkus(Map<String,Object> param){
		SearchResult<Map<String, Object>> result = new SearchResult<Map<String,Object>>();
		result.setRows(skuDao.selectSkuList());
		result.setTotal(skuDao.selectSkuList().size());
		return result;
	}

	@Override
	@Transactional
	public int updateByObject(List<Sku> list) {
		int i=0;
		for(Sku sku:list){
			i=skuDao.updateByPrimaryKeySelective(sku);
		}
		return i;
	} 
}

package com.tyiti.easycommerce.service;

import java.util.Map;

import com.tyiti.easycommerce.base.SearchResult;
import com.tyiti.easycommerce.entity.Tag;

public interface TagService {

	/**
	 * 按父标签id和层级获取标签树
	 * @param id
	 * @param level
	 * @return
	 */
	Map<String, Object> getListByParentId(Integer id, Integer level);

	/**
	 * 按父标签id和层级、商品条数获取标签树及商品列表
	 * @param id
	 * @param level
	 * @param limit
	 * @return
	 */
	Map<String, Object> getSkuListByParentId(Integer id, Integer level, Integer limit);

	/**
	 * 按id获取标签详情
	 * @param id
	 * @return
	 */
	Tag getById(Integer id);
	/**
	 * 获取商品的分类Id 列表
	 * @author :xulihui
	 * @since :2016年4月15日 下午5:53:34
	 * @param param
	 * @return
	 */
	SearchResult<Map<String, Object>> getByTagId(Map<String,Object> param);

	int getTagIdByCode(String code);
    
	
}

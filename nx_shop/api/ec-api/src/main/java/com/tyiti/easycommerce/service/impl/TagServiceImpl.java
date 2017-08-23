package com.tyiti.easycommerce.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tyiti.easycommerce.base.SearchResult;
import com.tyiti.easycommerce.entity.Tag;
import com.tyiti.easycommerce.repository.TagDao;
import com.tyiti.easycommerce.service.TagService;

@Service
public class TagServiceImpl implements TagService {
	@Autowired
	private TagDao tagDao;

	/**
	 * 按父标签id和层级获取标签树
	 * 
	 * @param id
	 * @param level
	 * @return
	 */
	public Map<String, Object> getListByParentId(Integer id, Integer level) {
		Map<String, Object> map = new HashMap<String, Object>();

		List<Tag> list = tagDao.getListByParentId(id, level);

		Integer lastDepth = 0;
		Integer lastParentId = 0;
		Map<String, List<Tag>> tagMap = new HashMap<String, List<Tag>>();
		List<Tag> tags = null;
		Tag tag = null;

		for (int i = list.size() - 1; i >= 0; i--) {
			tag = list.get(i);
			if (tag.getDepth() != lastDepth) {
				if (lastDepth != 0 && tag.getDepth() < lastDepth) {
					tags = tagMap.get(lastDepth + "_" + lastParentId);
					Collections.reverse(tags);
					tag.setChildrens(tags);
				}
				if (tagMap
						.containsKey(tag.getDepth() + "_" + tag.getParentId())) {
					tags = tagMap.get(tag.getDepth() + "_" + tag.getParentId());
				} else {
					tags = new ArrayList<Tag>();
				}
			}
			tags.add(tag);
			lastDepth = tag.getDepth();
			lastParentId = tag.getParentId();
			tagMap.put(tag.getDepth() + "_" + tag.getParentId(), tags);
		}
        if(list!=null&&list.size()!=0){
        	Collections.reverse(tagMap.get(1 + "_" + id));
    		map.put("list", tagMap.get(1 + "_" + id));
        }else{
    		map.put("code", 400);
    		map.put("messsge", "没有找到首页，请联系管理员");
    		return map;
        }
		tag = null;
		tags = null;
		tagMap = null;

		map.put("code", 200);
		map.put("messsge", "OK");
		return map;
	}

	/**
	 * 按父标签id和层级、商品条数获取标签树及商品列表
	 * 
	 * @param id
	 * @param level
	 * @param limit
	 * @return
	 */
	public Map<String, Object> getSkuListByParentId(Integer id, Integer level,
			Integer limit) {
		Map<String, Object> map = new HashMap<String, Object>();

		List<Tag> list = tagDao.getSkuListByParentId(id, level, limit);

		Integer lastDepth = 0;
		Integer lastParentId = 0;
		Map<String, List<Tag>> tagMap = new HashMap<String, List<Tag>>();
		List<Tag> tags = null;
		Tag tag = null;

		for (int i = list.size() - 1; i >= 0; i--) {
			tag = list.get(i);
			if (tag.getDepth() != lastDepth) {
				if (lastDepth != 0 && tag.getDepth() < lastDepth) {
					tags = tagMap.get(lastDepth + "_" + lastParentId);
					Collections.reverse(tags);
					tag.setChildrens(tags);
				}
				if (tagMap
						.containsKey(tag.getDepth() + "_" + tag.getParentId())) {
					tags = tagMap.get(tag.getDepth() + "_" + tag.getParentId());
				} else {
					tags = new ArrayList<Tag>();
				}
			}
			if (tag.getSkus() != null && tag.getSkus().size() > limit) {
				tag.setSkus(tag.getSkus().subList(0, limit));
			}
			tags.add(tag);
			lastDepth = tag.getDepth();
			lastParentId = tag.getParentId();
			tagMap.put(tag.getDepth() + "_" + tag.getParentId(), tags);
		}
        if(list!=null&&list.size()!=0){
    		Collections.reverse(tagMap.get(1 + "_" + id));
    		map.put("list", tagMap.get(1 + "_" + id));	
        }else{
        	map.put("list", tagMap.get(1 + "_" + id));	
        }


		tag = null;
		tags = null;
		tagMap = null;

		map.put("code", 200);
		map.put("messsge", "OK");
		return map;
	}

	/**
	 * 按id获取标签详情
	 * 
	 * @param id
	 * @return
	 */
	public Tag getById(Integer id) {
		return tagDao.getById(id);
	}

	/**
	 * 获取商品的分类Id 列表
	 */
	@Override
	public SearchResult<Map<String, Object>> getByTagId(
			Map<String, Object> param) {

		if (param.get("limit") != null && param.get("limit") != "") {
			param.put("limit",
					Integer.parseInt(String.valueOf(param.get("limit"))));
		}
		if (param.get("offset") != null && param.get("offset") != "") {
			param.put("offset",
					Integer.parseInt(String.valueOf(param.get("offset"))));
		}
		SearchResult<Map<String, Object>> searchResult = new SearchResult<Map<String, Object>>();
		searchResult.setRows(tagDao.selectTagLists(param));
		searchResult.setTotal(tagDao.selectTagCount(param));
		return searchResult;
	}

	@Override
	public int getTagIdByCode(String code) {
		// TODO Auto-generated method stub
		return tagDao.getTagIdByCode(code);
	}

}
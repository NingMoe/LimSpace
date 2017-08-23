package com.tyiti.easycommerce.controller;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tyiti.easycommerce.base.SearchResult;
import com.tyiti.easycommerce.service.TagService;

@Controller
public class TagController {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(TagController.class);

	@Autowired
	private TagService tagService;

	/**
	 * 按父标签及深度获取标签树
	 * 
	 * @param tagId
	 * @param level
	 * @return
	 */
	@RequestMapping(value = "/tags", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> listTagsByTagIdAndLevel(Integer tagId,
			Integer level) {
		Map<String, Object> data = new HashMap<String, Object>();
		Map<String, Object> res = tagService.getListByParentId(tagId, level);

		if ((Integer) res.get("code") != 200) {
			return res;
		}
		data = res;
		return data;
	}

	/**
	 * 按父标签及深度获取标签树及商品列表
	 * 
	 * @author :
	 * @since :2016年4月18日 下午2:23:48
	 * @param tagId
	 * @param level
	 * @param limit
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/tags/skus", method = RequestMethod.GET)
	public Map<String, Object> listTagSkusByTagIdAndLevelAndLimit(
			@RequestParam("code") String code,
			@RequestParam("level") Integer level,
			@RequestParam("limit") Integer limit) {
		Map<String, Object> data = new HashMap<String, Object>();
		int tagId = 0 ; 
		try {
			tagId= tagService.getTagIdByCode(code);
		} catch (Exception e) {
			data.put("code", 400);
			data.put("message", "code不存在");
			data.put("exception", e.getMessage());
			return data;
		}
		Map<String, Object> res = tagService.getSkuListByParentId(tagId, level,
				limit);

		if ( (Integer) res.get("code") != 200) {
			return res;
		}

		data = res;
		return data;
	}

	/**
	 * 获取商品的分类Id 列表
	 * 
	 * @author :xulihui
	 * @since :2016年4月15日 下午1:21:55
	 * @param tagId
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/tags/{tagId}/skus", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getByTagId(@PathVariable("tagId") Integer tagId,
			@RequestParam Map<String, Object> param) {

		Map<String, Object> map = new HashMap<String, Object>();

		SearchResult<Map<String, Object>> tagList = null;

		try {
			param.put("tagId", tagId);
			tagList = tagService.getByTagId(param);
		} catch (Exception e) {
			map.put("code", 400);
			map.put("exception", e.getMessage());
			map.put("message", "tag查询失败!");
			logger.error("tag查询失败!");
			return map;
		}
		map.put("code", 200);
		map.put("messsge", "OK");
		map.put("data", tagList);

		return map;
	}
}
